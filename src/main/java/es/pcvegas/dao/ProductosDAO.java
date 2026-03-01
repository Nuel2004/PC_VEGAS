package es.pcvegas.dao;

import es.pcvegas.beans.Producto;
import es.pcvegas.daofactory.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductosDAO implements IProductosDAO {

    @Override
    public List<Producto> getProductos() {
        List<Producto> lista = new ArrayList<>();
        // En una app real quizás limitarías esto (LIMIT 20)
        String sql = "SELECT * FROM productos";

        Connection conexion = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;

        try {
            conexion = ConnectionFactory.getConnection();
            preparada = conexion.prepareStatement(sql);
            resultado = preparada.executeQuery();

            while (resultado.next()) {
                Producto p = new Producto();
                p.setIdProducto(resultado.getInt("idproducto"));
                p.setIdCategoria(resultado.getInt("idcategoria"));
                p.setNombre(resultado.getString("nombre"));
                p.setDescripcion(resultado.getString("descripcion"));
                p.setPrecio(resultado.getDouble("precio"));
                p.setMarca(resultado.getString("marca"));
                p.setImagen(resultado.getString("imagen"));
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(resultado, preparada, conexion);
        }
        return lista;
    }

    @Override
    public List<Producto> getProductosPorCategoria(int idCategoria) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE idcategoria = ?";

        Connection conexion = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;

        try {
            conexion = ConnectionFactory.getConnection();
            preparada = conexion.prepareStatement(sql);
            preparada.setInt(1, idCategoria);
            resultado = preparada.executeQuery();

            while (resultado.next()) {
                Producto p = new Producto();
                p.setIdProducto(resultado.getInt("idproducto"));
                p.setIdCategoria(resultado.getInt("idcategoria"));
                p.setNombre(resultado.getString("nombre"));
                p.setDescripcion(resultado.getString("descripcion"));
                p.setPrecio(resultado.getDouble("precio"));
                p.setMarca(resultado.getString("marca"));
                p.setImagen(resultado.getString("imagen"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(resultado, preparada, conexion);
        }
        return lista;
    }

    // Puedes implementar getProductosOferta() y getProductoDetalle() de forma similar
    @Override
    public List<Producto> getProductosOferta() {
        return new ArrayList<>(); // Pendiente de implementación
    }

    @Override
    public Producto getProductoDetalle(int idProducto) {
        Producto p = null;
        String sql = "SELECT * FROM productos WHERE idproducto = ?";

        Connection conexion = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;

        try {
            conexion = ConnectionFactory.getConnection();
            preparada = conexion.prepareStatement(sql);
            preparada.setInt(1, idProducto);
            resultado = preparada.executeQuery();

            if (resultado.next()) {
                p = new Producto();
                p.setIdProducto(resultado.getInt("idproducto"));
                p.setIdCategoria(resultado.getInt("idcategoria"));
                p.setNombre(resultado.getString("nombre"));
                p.setDescripcion(resultado.getString("descripcion"));
                p.setPrecio(resultado.getDouble("precio"));
                p.setMarca(resultado.getString("marca"));
                p.setImagen(resultado.getString("imagen"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Usamos tu método privado close
            this.close(resultado, preparada, conexion);
        }
        return p;
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

    @Override
    public List<Producto> getProductosAleatorios() {
        List<Producto> lista = new ArrayList<>();
        // Magia de MySQL: Ordenar aleatoriamente y coger 8
        String sql = "SELECT * FROM productos ORDER BY RAND() LIMIT 9";

        Connection conexion = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;

        try {
            conexion = ConnectionFactory.getConnection();
            preparada = conexion.prepareStatement(sql);
            resultado = preparada.executeQuery();

            while (resultado.next()) {
                Producto p = new Producto();
                p.setIdProducto(resultado.getInt("idproducto"));
                p.setIdCategoria(resultado.getInt("idcategoria"));
                p.setNombre(resultado.getString("nombre"));
                p.setDescripcion(resultado.getString("descripcion"));
                p.setPrecio(resultado.getDouble("precio"));
                p.setMarca(resultado.getString("marca"));
                p.setImagen(resultado.getString("imagen"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(resultado, preparada, conexion);
        }
        return lista;
    }

    @Override
    public java.util.List<String> getMarcas() {
        java.util.List<String> marcas = new java.util.ArrayList<>();
        String sql = "SELECT DISTINCT marca FROM productos ORDER BY marca ASC";

        Connection conexion = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;

        try {
            conexion = ConnectionFactory.getConnection();
            preparada = conexion.prepareStatement(sql);
            resultado = preparada.executeQuery();

            while (resultado.next()) {
                marcas.add(resultado.getString("marca"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(resultado, preparada, conexion);
        }
        return marcas;
    }

    @Override
    public double[] getRangoPrecios() {
        // Por defecto devolvemos 0 y 10000 por si la tienda estuviera vacía
        double[] rango = new double[]{0.0, 10000.0};
        String sql = "SELECT MIN(precio) as min_precio, MAX(precio) as max_precio FROM productos";

        Connection conexion = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;

        try {
            conexion = ConnectionFactory.getConnection();
            preparada = conexion.prepareStatement(sql);
            resultado = preparada.executeQuery();

            if (resultado.next()) {
                rango[0] = resultado.getDouble("min_precio");
                rango[1] = resultado.getDouble("max_precio");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(resultado, preparada, conexion);
        }
        return rango;
    }

    @Override
    public List<Producto> filtrarProductos(String busqueda, String[] marcas, double precioMin, double precioMax, Integer idCategoria) {
        List<Producto> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM productos WHERE precio >= ? AND precio <= ?");
        List<Object> parametros = new ArrayList<>();
        parametros.add(precioMin);
        parametros.add(precioMax);

        // 1) Búsqueda por nombre
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            sql.append(" AND nombre LIKE ?");
            parametros.add("%" + busqueda.trim() + "%");
        }

        // 2) Marcas múltiples (IN)
        // Si no selecciona ninguna marca -> no filtramos por marca (equivale a todas)
        if (marcas != null && marcas.length > 0) {

            // Filtrar valores vacíos por seguridad
            List<String> marcasValidas = new ArrayList<>();
            for (String m : marcas) {
                if (m != null && !m.trim().isEmpty()) {
                    marcasValidas.add(m.trim());
                }
            }

            if (!marcasValidas.isEmpty()) {
                sql.append(" AND marca IN (");
                for (int i = 0; i < marcasValidas.size(); i++) {
                    if (i > 0) {
                        sql.append(",");
                    }
                    sql.append("?");
                    parametros.add(marcasValidas.get(i));
                }
                sql.append(")");
            }
        }

        // 3) Categoría
        if (idCategoria != null && idCategoria > 0) {
            sql.append(" AND idcategoria = ?");
            parametros.add(idCategoria);
        }

        Connection conexion = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;

        try {
            conexion = ConnectionFactory.getConnection();
            preparada = conexion.prepareStatement(sql.toString());

            for (int i = 0; i < parametros.size(); i++) {
                preparada.setObject(i + 1, parametros.get(i));
            }

            resultado = preparada.executeQuery();

            while (resultado.next()) {
                Producto p = new Producto();
                p.setIdProducto(resultado.getInt("idproducto"));
                p.setIdCategoria(resultado.getInt("idcategoria"));
                p.setNombre(resultado.getString("nombre"));
                p.setDescripcion(resultado.getString("descripcion"));
                p.setPrecio(resultado.getDouble("precio"));
                p.setMarca(resultado.getString("marca"));
                p.setImagen(resultado.getString("imagen"));
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(resultado, preparada, conexion);
        }

        return lista;
    }

}
