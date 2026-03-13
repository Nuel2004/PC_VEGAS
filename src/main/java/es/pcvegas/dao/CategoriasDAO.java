package es.pcvegas.dao;

import es.pcvegas.beans.Categoria;
import es.pcvegas.daofactory.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de ICategoriasDAO utilizando JDBC. Gestiona la interacción con
 * la tabla 'categorias'.
 *
 * * @author manuel
 */
public class CategoriasDAO implements ICategoriasDAO {

    /**
     * Recupera todas las categorías de la base de datos. Realiza una consulta
     * SELECT sobre la tabla 'categorias'.
     *
     * * @return Lista de objetos Categoria con sus datos (id, nombre, imagen).
     */
    @Override
    public List<Categoria> getCategorias() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT idcategoria, nombre, imagen FROM categorias";

        Connection conexion = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;

        try {
            conexion = ConnectionFactory.getConnection();
            preparada = conexion.prepareStatement(sql);
            resultado = preparada.executeQuery();

            while (resultado.next()) {
                Categoria c = new Categoria();
                c.setIdCategoria(resultado.getInt("idcategoria"));
                c.setNombre(resultado.getString("nombre"));
                c.setImagen(resultado.getString("imagen"));
                lista.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(resultado, preparada, conexion);
        }
        return lista;
    }

    /**
     * Implementación opcional para cierre manual de recursos específicos del
     * DAO.
     */
    @Override
    public void close() {
    }

    /**
     * Método auxiliar privado para cerrar de forma segura los recursos JDBC.
     *
     * * @param rs ResultSet a cerrar.
     * @param ps PreparedStatement a cerrar.
     * @param conn Connection a cerrar.
     */
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
}
