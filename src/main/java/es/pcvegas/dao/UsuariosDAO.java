package es.pcvegas.dao;

import es.pcvegas.beans.Usuario;
import es.pcvegas.daofactory.ConnectionFactory;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Implementación JDBC de IUsuariosDAO. Maneja el ciclo de vida del usuario en
 * la BD (login, registro, updates).
 *
 * * @author manuel
 */
public class UsuariosDAO implements IUsuariosDAO {

    /**
     * Verifica credenciales contra la tabla usuarios.
     *
     * * @param email Email del usuario.
     * @param passwordMD5 Hash de la contraseña.
     * @return Usuario logueado o null.
     */
    @Override
    public Usuario login(String email, String passwordMD5) {
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Usuario u = null;

        try {
            cn = ConnectionFactory.getConnection();
            String sql = "SELECT * FROM usuarios WHERE email=? AND password=?";
            ps = cn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, passwordMD5);
            rs = ps.executeQuery();

            if (rs.next()) {
                u = mapearUsuario(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, cn);
        }
        return u;
    }

    /**
     * Inserta un nuevo registro de usuario. Recupera el ID autogenerado.
     *
     * * @param u Objeto Usuario con datos.
     * @return ID del usuario nuevo.
     */
    @Override
    public int registrar(Usuario u) {
        int id = -1;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            cn = ConnectionFactory.getConnection();
            String sql = "INSERT INTO usuarios (email, password, nombre, apellidos, nif, telefono, direccion, codigo_postal, localidad, provincia, ultimo_acceso, avatar) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NULL, ?)";

            ps = cn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, u.getEmail());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getNombre());
            ps.setString(4, u.getApellidos());
            ps.setString(5, u.getNif());
            ps.setString(6, u.getTelefono());
            ps.setString(7, u.getDireccion());
            ps.setString(8, u.getCodigoPostal());
            ps.setString(9, u.getLocalidad());
            ps.setString(10, u.getProvincia());
            ps.setString(11, u.getAvatar()); // Suele ser "default.jpg" al inicio

            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, cn);
        }
        return id;
    }

    /**
     * Recupera un usuario por su email.
     *
     * * @param email El email a buscar.
     * @return El usuario encontrado o null.
     */
    @Override
    public Usuario getUsuarioPorEmail(String email) {
        Usuario u = null;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = ConnectionFactory.getConnection();
            String sql = "SELECT * FROM usuarios WHERE email=?";
            ps = cn.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                u = mapearUsuario(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, cn);
        }
        return u;
    }

    /**
     * Actualiza únicamente el campo avatar.
     *
     * * @param idUsuario ID del usuario.
     * @param nombreArchivo Nuevo nombre de imagen.
     * @return True en caso de éxito.
     */
    @Override
    public boolean actualizarAvatar(int idUsuario, String nombreArchivo) {
        boolean exito = false;
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = ConnectionFactory.getConnection();
            String sql = "UPDATE usuarios SET avatar=? WHERE idusuario=?";
            ps = cn.prepareStatement(sql);
            ps.setString(1, nombreArchivo);
            ps.setInt(2, idUsuario);
            int filas = ps.executeUpdate();
            exito = (filas > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(null, ps, cn);
        }
        return exito;
    }

    /**
     * Actualiza los datos del perfil y opcionalmente el avatar.
     *
     * * @param u Usuario con datos nuevos.
     * @param nuevoAvatar Nombre del nuevo avatar (si es null, se ignora).
     * @return True en caso de éxito.
     */
    @Override
    public boolean actualizarPerfil(Usuario u, String nuevoAvatar) {
        boolean exito = false;
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = ConnectionFactory.getConnection();
            StringBuilder sql = new StringBuilder("UPDATE usuarios SET nombre=?, apellidos=?, nif=?, telefono=?, direccion=?, codigo_postal=?, localidad=?, provincia=?");

            if (nuevoAvatar != null) {
                sql.append(", avatar=?");
            }
            sql.append(" WHERE idusuario=?");

            ps = cn.prepareStatement(sql.toString());
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellidos());
            ps.setString(3, u.getNif());
            ps.setString(4, u.getTelefono());
            ps.setString(5, u.getDireccion());
            ps.setString(6, u.getCodigoPostal());
            ps.setString(7, u.getLocalidad());
            ps.setString(8, u.getProvincia());

            int i = 9;
            if (nuevoAvatar != null) {
                ps.setString(i++, nuevoAvatar);
            }
            ps.setInt(i, u.getIdUsuario());

            int filas = ps.executeUpdate();
            exito = (filas > 0);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(null, ps, cn);
        }
        return exito;
    }

    /**
     * Borra físicamente un usuario de la BD.
     *
     * * @param idusuario ID a borrar.
     */
    @Override
    public void eliminar(int idusuario) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = ConnectionFactory.getConnection();
            String sql = "DELETE FROM usuarios WHERE idusuario=?";
            ps = cn.prepareStatement(sql);
            ps.setInt(1, idusuario);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(null, ps, cn);
        }
    }

    /**
     * Actualiza el timestamp de último acceso.
     *
     * * @param idusuario ID del usuario.
     * @param fecha Fecha util.Date a convertir a SQL Timestamp.
     */
    @Override
    public void actualizarUltimoAcceso(int idusuario, Date fecha) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = ConnectionFactory.getConnection();
            String sql = "UPDATE usuarios SET ultimo_acceso=? WHERE idusuario=?";
            ps = cn.prepareStatement(sql);
            ps.setTimestamp(1, new Timestamp(fecha.getTime()));
            ps.setInt(2, idusuario);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(null, ps, cn);
        }
    }

    /**
     * Método auxiliar para mapear ResultSet a objeto Usuario. Evita duplicidad
     * de código.
     *
     * * @param rs ResultSet posicionado.
     * @return Objeto Usuario hidratado.
     * @throws SQLException Si hay error de acceso.
     */
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("idusuario"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        u.setNombre(rs.getString("nombre"));
        u.setApellidos(rs.getString("apellidos"));
        u.setNif(rs.getString("nif"));
        u.setTelefono(rs.getString("telefono"));
        u.setDireccion(rs.getString("direccion"));
        u.setCodigoPostal(rs.getString("codigo_postal"));
        u.setLocalidad(rs.getString("localidad"));
        u.setProvincia(rs.getString("provincia"));
        u.setUltimoAcceso(rs.getTimestamp("ultimo_acceso"));
        u.setAvatar(rs.getString("avatar"));
        return u;
    }

    private void close(ResultSet rs, PreparedStatement ps, Connection cn) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (cn != null) {
                cn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
