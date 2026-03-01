package es.pcvegas.dao;

import es.pcvegas.beans.Categoria;
import es.pcvegas.daofactory.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriasDAO implements ICategoriasDAO {

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

    @Override
    public void close() {
        // Implementación opcional si se requiere cierre manual específico
    }

    // Método auxiliar para cerrar recursos
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
