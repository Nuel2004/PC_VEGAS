package es.pcvegas.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pedido implements Serializable {

    private int idPedido;
    private Date fecha;
    private String estado; // 'c' = carrito, 'f' = finalizado
    private int idUsuario;
    private double importe; // Base imponible
    private double iva; // Importe del IVA

    // Lista para contener las líneas del pedido (EL CARRITO)
    private List<LineaPedido> lineas;
    // Objeto Usuario asociado (útil para ver quién hizo el pedido)
    private Usuario usuarioObj;

    public Pedido() {
        this.estado = "c"; // Por defecto es carrito
        this.lineas = new ArrayList<>();
        this.importe = 0.0;
        this.iva = 0.0;
    }

    // Método auxiliar para calcular totales del carrito en memoria
    public double getTotal() {
        return this.importe + this.iva;
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
