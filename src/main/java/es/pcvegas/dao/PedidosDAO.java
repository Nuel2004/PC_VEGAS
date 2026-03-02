package es.pcvegas.dao;

import es.pcvegas.beans.LineaPedido;
import es.pcvegas.beans.Pedido;
import es.pcvegas.beans.Producto;
import es.pcvegas.daofactory.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidosDAO implements IPedidosDAO {

    private static final String ESTADO_CARRITO = "c";
    private static final String ESTADO_FINAL = "f";

    @Override
    public int crearPedido(Pedido pedido) {
        int idGenerado = -1;

        String sql = "INSERT INTO pedidos (fecha, estado, idusuario, importe, iva) "
                + "VALUES (NOW(), ?, ?, 0, 0)";

        try (Connection conexion = ConnectionFactory.getConnection(); PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, ESTADO_CARRITO);
            ps.setInt(2, pedido.getIdUsuario());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    idGenerado = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idGenerado;
    }

    @Override
    public void actualizarEstado(int idPedido, String estado) {
        String sql = "UPDATE pedidos SET estado = ? WHERE idpedido = ?";

        try (Connection conexion = ConnectionFactory.getConnection(); PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, idPedido);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Pedido getPedidoEnCurso(int idUsuario) {
        Pedido pedido = null;

        String sql = "SELECT idpedido, fecha, estado, idusuario, importe, iva "
                + "FROM pedidos "
                + "WHERE idusuario = ? AND estado = ? "
                + "ORDER BY fecha DESC "
                + "LIMIT 1";

        try (Connection conexion = ConnectionFactory.getConnection(); PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setString(2, ESTADO_CARRITO);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pedido = new Pedido();
                    pedido.setIdPedido(rs.getInt("idpedido"));
                    pedido.setFecha(rs.getDate("fecha"));
                    pedido.setEstado(rs.getString("estado"));
                    pedido.setIdUsuario(rs.getInt("idusuario"));
                    pedido.setImporte(rs.getDouble("importe"));
                    pedido.setIva(rs.getDouble("iva"));

                    pedido.setLineas(getLineasPedido(pedido.getIdPedido()));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedido;
    }

    @Override
    public void cerrarPedido(Pedido pedido) {
        // Cierra el pedido: actualiza importes + estado final
        String sql = "UPDATE pedidos SET importe = ?, iva = ?, estado = ? WHERE idpedido = ?";

        try (Connection conexion = ConnectionFactory.getConnection(); PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setDouble(1, pedido.getImporte());
            ps.setDouble(2, pedido.getIva());
            ps.setString(3, ESTADO_FINAL);
            ps.setInt(4, pedido.getIdPedido());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void agregarLinea(LineaPedido linea) {
        String sql = "INSERT INTO lineaspedidos (idpedido, idproducto, cantidad) VALUES (?, ?, ?)";

        try (Connection conexion = ConnectionFactory.getConnection(); PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, linea.getIdPedido());
            ps.setInt(2, linea.getIdProducto());
            ps.setInt(3, linea.getCantidad());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminarLinea(int idLinea) {
        String sql = "DELETE FROM lineaspedidos WHERE idlinea = ?";

        try (Connection conexion = ConnectionFactory.getConnection(); PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idLinea);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void vaciarLineas(int idPedido) {
        String sql = "DELETE FROM lineaspedidos WHERE idpedido = ?";

        try (Connection conexion = ConnectionFactory.getConnection(); PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idPedido);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<LineaPedido> getLineasPedido(int idPedido) {
        List<LineaPedido> lineas = new ArrayList<>();

        String sql = "SELECT l.idlinea, l.idpedido, l.idproducto, l.cantidad, "
                + "p.nombre, p.imagen, p.precio, p.marca "
                + "FROM lineaspedidos l "
                + "INNER JOIN productos p ON l.idproducto = p.idproducto "
                + "WHERE l.idpedido = ?";

        try (Connection conexion = ConnectionFactory.getConnection(); PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idPedido);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LineaPedido linea = new LineaPedido();
                    linea.setIdLinea(rs.getInt("idlinea"));
                    linea.setIdPedido(rs.getInt("idpedido"));
                    linea.setIdProducto(rs.getInt("idproducto"));
                    linea.setCantidad(rs.getInt("cantidad"));

                    Producto p = new Producto();
                    p.setIdProducto(rs.getInt("idproducto"));
                    p.setNombre(rs.getString("nombre"));
                    p.setImagen(rs.getString("imagen"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setMarca(rs.getString("marca"));

                    linea.setProductoObj(p);
                    lineas.add(linea);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lineas;
    }

    // ----------------------------------------------------------------------
    // REGISTRAR PEDIDO (TRANSACCIÓN) -> INSERT pedidos + INSERT lineaspedidos
    // ----------------------------------------------------------------------
    @Override
    public int registrarPedido(Pedido carrito) {

        if (carrito == null || carrito.getIdUsuario() <= 0) {
            return -1;
        }
        if (carrito.getLineas() == null || carrito.getLineas().isEmpty()) {
            return -1;
        }

        String sqlInsertPedido = "INSERT INTO pedidos (fecha, estado, idusuario, importe, iva) "
                + "VALUES (NOW(), ?, ?, ?, ?)";
        String sqlInsertLinea = "INSERT INTO lineaspedidos (idpedido, idproducto, cantidad) "
                + "VALUES (?, ?, ?)";

        Connection conn = null;
        PreparedStatement psPedido = null;
        PreparedStatement psLinea = null;
        ResultSet rsKeys = null;

        int idPedidoGenerado = -1;

        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            // Recalcular importes + IVA fijo (base imponible)
            double subtotal = 0.0;
            for (LineaPedido l : carrito.getLineas()) {
                if (l != null && l.getProductoObj() != null) {
                    subtotal += l.getProductoObj().getPrecio() * l.getCantidad();
                }
            }
            carrito.setImporte(subtotal);
            carrito.setIva(subtotal * 0.21);

            // 1) Insert pedido en estado FINAL
            psPedido = conn.prepareStatement(sqlInsertPedido, Statement.RETURN_GENERATED_KEYS);
            psPedido.setString(1, ESTADO_FINAL);
            psPedido.setInt(2, carrito.getIdUsuario());
            psPedido.setDouble(3, carrito.getImporte());
            psPedido.setDouble(4, carrito.getIva());

            int filas = psPedido.executeUpdate();
            if (filas != 1) {
                throw new SQLException("No se insertó el pedido.");
            }

            rsKeys = psPedido.getGeneratedKeys();
            if (!rsKeys.next()) {
                throw new SQLException("No se obtuvo idpedido.");
            }
            idPedidoGenerado = rsKeys.getInt(1);

            // 2) Insert líneas
            psLinea = conn.prepareStatement(sqlInsertLinea);

            for (LineaPedido l : carrito.getLineas()) {
                if (l == null) {
                    continue;
                }

                int idProducto = l.getIdProducto();
                if (idProducto <= 0 && l.getProductoObj() != null) {
                    idProducto = l.getProductoObj().getIdProducto();
                }

                if (idProducto <= 0 || l.getCantidad() <= 0) {
                    throw new SQLException("Línea inválida (idProducto/cantidad).");
                }

                psLinea.setInt(1, idPedidoGenerado);
                psLinea.setInt(2, idProducto);
                psLinea.setInt(3, l.getCantidad());
                psLinea.addBatch();
            }

            psLinea.executeBatch();

            conn.commit();
            return idPedidoGenerado;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ignored) {
                }
            }
            e.printStackTrace();
            return -1;

        } finally {
            if (rsKeys != null) try {
                rsKeys.close();
            } catch (SQLException ignored) {
            }
            if (psLinea != null) try {
                psLinea.close();
            } catch (SQLException ignored) {
            }
            if (psPedido != null) try {
                psPedido.close();
            } catch (SQLException ignored) {
            }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException ignored) {
                }
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }
    @Override
    public java.util.List<Pedido> getPedidosUsuario(int idUsuario) {
        java.util.List<Pedido> lista = new java.util.ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE idusuario = ? ORDER BY fecha DESC";
        
        Connection conexion = null;
        PreparedStatement preparada = null;
        ResultSet rs = null;

        try {
            conexion = ConnectionFactory.getConnection();
            preparada = conexion.prepareStatement(sql);
            preparada.setInt(1, idUsuario);
            rs = preparada.executeQuery();

            while (rs.next()) {
                Pedido p = new Pedido();
                p.setIdPedido(rs.getInt("idpedido"));
                p.setFecha(rs.getDate("fecha"));
                p.setEstado(rs.getString("estado"));
                p.setIdUsuario(rs.getInt("idusuario"));
                p.setImporte(rs.getDouble("importe"));
                p.setIva(rs.getDouble("iva"));
                
                // ¡IMPORTANTE! Cargamos los productos de este pedido para poder pintarlos
                // Reutilizamos el método que ya tienes en esta misma clase
                p.setLineas(this.getLineasPedido(p.getIdPedido()));
                
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public void close() {
        // No-op (se usa try-with-resources en cada método)
    }
}
