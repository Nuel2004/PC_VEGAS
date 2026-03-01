package es.pcvegas.beans;

import java.io.Serializable;

public class LineaPedido implements Serializable {

    private int idLinea;
    private int idPedido;
    private int idProducto;
    private int cantidad;

    // Objeto Producto asociado (necesario para pintar el nombre y precio en el carrito)
    private Producto productoObj;

    public LineaPedido() {
    }

    // Getters y Setters
    public int getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(int idLinea) {
        this.idLinea = idLinea;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProductoObj() {
        return productoObj;
    }

    public void setProductoObj(Producto productoObj) {
        this.productoObj = productoObj;
    }
}
