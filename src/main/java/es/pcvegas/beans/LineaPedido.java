package es.pcvegas.beans;

import java.io.Serializable;

/**
 * Representa una línea de detalle dentro de un pedido. Contiene la información
 * de qué producto se ha pedido y en qué cantidad.
 *
 * * @author manuel
 */
public class LineaPedido implements Serializable {

    /**
     * Identificador único de la línea de pedido.
     */
    private int idLinea;

    /**
     * Identificador del pedido al que pertenece esta línea.
     */
    private int idPedido;

    /**
     * Identificador del producto incluido en esta línea.
     */
    private int idProducto;

    /**
     * Cantidad de unidades del producto.
     */
    private int cantidad;

    /**
     * * Objeto Producto asociado. Campo auxiliar no persistente en la tabla de
     * líneas, pero necesario para mostrar detalles (nombre, precio, imagen) en
     * la vista del carrito.
     */
    private Producto productoObj;

    /**
     * Constructor vacío por defecto.
     */
    public LineaPedido() {
    }

    /**
     * Obtiene el identificador de la línea de pedido.
     *
     * * @return El ID de la línea.
     */
    public int getIdLinea() {
        return idLinea;
    }

    /**
     * Establece el identificador de la línea de pedido.
     *
     * * @param idLinea El ID a establecer.
     */
    public void setIdLinea(int idLinea) {
        this.idLinea = idLinea;
    }

    /**
     * Obtiene el identificador del pedido asociado.
     *
     * * @return El ID del pedido padre.
     */
    public int getIdPedido() {
        return idPedido;
    }

    /**
     * Establece el identificador del pedido asociado.
     *
     * * @param idPedido El ID del pedido.
     */
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    /**
     * Obtiene el identificador del producto de esta línea.
     *
     * * @return El ID del producto.
     */
    public int getIdProducto() {
        return idProducto;
    }

    /**
     * Establece el identificador del producto.
     *
     * * @param idProducto El ID del producto.
     */
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    /**
     * Obtiene la cantidad de unidades solicitadas.
     *
     * * @return La cantidad.
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad de unidades.
     *
     * * @param cantidad La cantidad a establecer.
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el objeto Producto completo asociado a esta línea. Útil para
     * visualización en el frontend.
     *
     * * @return El objeto Producto.
     */
    public Producto getProductoObj() {
        return productoObj;
    }

    /**
     * Asocia un objeto Producto completo a esta línea.
     *
     * * @param productoObj El objeto Producto a asociar.
     */
    public void setProductoObj(Producto productoObj) {
        this.productoObj = productoObj;
    }
}
