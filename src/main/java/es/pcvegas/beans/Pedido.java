package es.pcvegas.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Representa un pedido realizado por un usuario. Puede actuar como un carrito
 * de compras temporal (estado 'c') o como un pedido finalizado (estado 'f').
 *
 * @author manuel
 */
public class Pedido implements Serializable {

    private int idPedido;
    private Date fecha;
    private String estado;
    private int idUsuario;
    private double importe;
    private double iva;

    private List<LineaPedido> lineas;
    private Usuario usuarioObj;

    public Pedido() {
        this.estado = "c"; 
        this.lineas = new ArrayList<>();
        this.importe = 0.0;
        this.iva = 0.0;
    }

    // Método auxiliar para calcular totales del carrito en memoria
    public double getTotal() {
        return this.importe + this.iva;
    }

    /**
     * NUEVO: Calcula la cantidad total de artículos sumando las cantidades de
     * cada línea. Útil para mostrar el número real en el icono del carrito del
     * header.
     *
     * @return El número total de unidades físicas en el carrito.
     */
    public int getTotalArticulos() {
        int total = 0;
        if (this.lineas != null) {
            for (LineaPedido linea : this.lineas) {
                total += linea.getCantidad();
            }
        }
        return total;
    }

    // Getters y Setters
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public List<LineaPedido> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaPedido> lineas) {
        this.lineas = lineas;
    }

    public Usuario getUsuarioObj() {
        return usuarioObj;
    }

    public void setUsuarioObj(Usuario usuarioObj) {
        this.usuarioObj = usuarioObj;
    }
}
