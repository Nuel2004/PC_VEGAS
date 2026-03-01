package es.pcvegas.dao;

import es.pcvegas.beans.Usuario;
import es.pcvegas.daofactory.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuariosDAO implements IUsuariosDAO {

    /**
     * CORREGIDO: Ahora devuelve boolean para saber si el registro fue exitoso.
     */
    @Override
    public boolean registrar(Usuario usuario) {
        boolean exito = false;
        String sql = "INSERT INTO usuarios (email, password, nombre, apellidos, nif, telefono, direccion, codigo_postal, localidad, provincia, ultimo_acceso, avatar) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?)";

        Connection conexion = null;
        PreparedStatement preparada = null;

        try {
            conexion = ConnectionFactory.getConnection();
            conexion.setAutoCommit(false); // Inicio transacción

            preparada = conexion.prepareStatement(sql);

            preparada.setString(1, usuario.getEmail());
            preparada.setString(2, usuario.getPassword());
            preparada.setString(3, usuario.getNombre());
            preparada.setString(4, usuario.getApellidos());
            preparada.setString(5, usuario.getNif());
            preparada.setString(6, usuario.getTelefono());
            preparada.setString(7, usuario.getDireccion());

            String cp = usuario.getCodigoPostal();
            if (cp == null || cp.trim().isEmpty()) {
                cp = "00000";
            }
            preparada.setString(8, cp.trim());

            String localidad = usuario.getLocalidad();
            if (localidad == null) {
                localidad = "";
            }
            preparada.setString(9, localidad.trim());

            String provincia = usuario.getProvincia();
            if (provincia == null) {
                provincia = "";
            }
            preparada.setString(10, provincia.trim());

            String avatar = usuario.getAvatar();
            if (avatar == null || avatar.trim().isEmpty()) {
                avatar = "default.jpg";
            }
            preparada.setString(11, avatar.trim());

            int filas = preparada.executeUpdate();
            conexion.commit(); // Confirmamos cambios

            if (filas > 0) {
                exito = true;
            }

        } catch (SQLException e) {
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            // No lanzamos RuntimeException para que el Controller pueda manejar el false
        } finally {
            if (conexion != null) {
                try {
                    conexion.setAutoCommit(true);
                } catch (SQLException ignored) {
                }
            }
            close(null, preparada, conexion);
        }

        return exito;
    }

    @Override
    public Usuario login(String email, String password) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE email = ? AND password = ?";

        Connection conexion = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;

        try {
            conexion = ConnectionFactory.getConnection();
            preparada = conexion.prepareStatement(sql);
            preparada.setString(1, email);
            preparada.setString(2, password);
            resultado = preparada.executeQuery();

            if (resultado.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(resultado.getInt("idusuario"));
                usuario.setEmail(resultado.getString("email"));
                usuario.setNombre(resultado.getString("nombre"));
                usuario.setApellidos(resultado.getString("apellidos"));
                usuario.setNif(resultado.getString("nif"));
                usuario.setTelefono(resultado.getString("telefono"));
                usuario.setDireccion(resultado.getString("direccion"));
                usuario.setCodigoPostal(resultado.getString("codigo_postal"));
                usuario.setLocalidad(resultado.getString("localidad"));
                usuario.setProvincia(resultado.getString("provincia"));

                String avatarBD = resultado.getString("avatar");
                if (avatarBD != null && !avatarBD.trim().isEmpty()) {
                    usuario.setAvatar(avatarBD);
                } else {
                    usuario.setAvatar("default.jpg");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(resultado, preparada, conexion);
        }

        return usuario;
    }

    @Override
    public Usuario getUsuarioPorEmail(String email) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE email = ?";

        Connection conexion = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;

        try {
            conexion = ConnectionFactory.getConnection();
            preparada = conexion.prepareStatement(sql);
            preparada.setString(1, email);
            resultado = preparada.executeQuery();

            if (resultado.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(resultado.getInt("idusuario"));
                usuario.setEmail(resultado.getString("email"));
                usuario.setNombre(resultado.getString("nombre"));
                // ... (puedes rellenar el resto si lo necesitas)
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(resultado, preparada, conexion);
        }

        return usuario;
    }

    @Override
    public void actualizarAvatar(int idUsuario, String avatar) {
        String sql = "UPDATE usuarios SET avatar = ? WHERE idusuario = ?";

        Connection conexion = null;
        PreparedStatement preparada = null;

        try {
            conexion = ConnectionFactory.getConnection();
            preparada = conexion.prepareStatement(sql);

            if (avatar == null || avatar.trim().isEmpty()) {
                avatar = "default.jpg";
            }

            preparada.setString(1, avatar.trim());
            preparada.setInt(2, idUsuario);
            preparada.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(null, preparada, conexion);
        }
    }

    @Override
    public void eliminar(int idUsuario) {
        String sql = "DELETE FROM usuarios WHERE idusuario = ?";

        Connection conexion = null;
        PreparedStatement preparada = null;

        try {
            conexion = ConnectionFactory.getConnection();
            conexion.setAutoCommit(false);

            preparada = conexion.prepareStatement(sql);
            preparada.setInt(1, idUsuario);
            preparada.executeUpdate();

            conexion.commit();

        } catch (SQLException e) {
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conexion != null) {
                try {
                    conexion.setAutoCommit(true);
                } catch (SQLException ignored) {
                }
            }
            close(null, preparada, conexion);
        }
    }

    @Override
    public void close() {
        // No-op
    }

    private void close(ResultSet rs, PreparedStatement ps, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Añádelo también en la interfaz IUsuariosDAO si la tienes
    public boolean actualizarPerfil(es.pcvegas.beans.Usuario u) {
        boolean exito = false;
        String sql = "UPDATE usuarios SET nombre=?, apellidos=?, nif=?, telefono=?, "
                + "direccion=?, codigo_postal=?, localidad=?, provincia=? "
                + "WHERE idusuario=?";

        Connection conexion = null;
        PreparedStatement preparada = null;

        try {
            conexion = ConnectionFactory.getConnection();
            preparada = conexion.prepareStatement(sql);

            preparada.setString(1, u.getNombre());
            preparada.setString(2, u.getApellidos());
            preparada.setString(3, u.getNif());
            preparada.setString(4, u.getTelefono());
            preparada.setString(5, u.getDireccion());
            preparada.setString(6, u.getCodigoPostal());
            preparada.setString(7, u.getLocalidad());
            preparada.setString(8, u.getProvincia());

            // EL WHERE
            preparada.setInt(9, u.getIdUsuario());

            int filas = preparada.executeUpdate();
            if (filas > 0) {
                exito = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(null, preparada, conexion);
        }
        return exito;
    }

    @Override
    public boolean actualizarPerfil(es.pcvegas.beans.Usuario u, String nuevoAvatarFilename) {
        boolean exito = false;
        String sql = "UPDATE usuarios SET nombre=?, apellidos=?, nif=?, telefono=?, "
                + "direccion=?, codigo_postal=?, localidad=?, provincia=?, avatar=? "
                + "WHERE idusuario=?";

        Connection conexion = null;
        PreparedStatement preparada = null;

        try {
            conexion = ConnectionFactory.getConnection();
            preparada = conexion.prepareStatement(sql);

            preparada.setString(1, u.getNombre());
            preparada.setString(2, u.getApellidos());
            preparada.setString(3, u.getNif());
            preparada.setString(4, u.getTelefono());
            preparada.setString(5, u.getDireccion());
            preparada.setString(6, u.getCodigoPostal());
            preparada.setString(7, u.getLocalidad());
            preparada.setString(8, u.getProvincia());

            // EL AVATAR
            preparada.setString(9, nuevoAvatarFilename);

            // EL WHERE
            preparada.setInt(10, u.getIdUsuario());

            int filas = preparada.executeUpdate();
            if (filas > 0) {
                exito = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(null, preparada, conexion);
        }
        return exito;
    }
}
