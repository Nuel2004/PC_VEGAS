package es.pcvegas.dao;

import es.pcvegas.beans.Pedido;
import es.pcvegas.beans.LineaPedido;
import java.util.List;

public interface IPedidosDAO {

    // Métodos para el Pedido (Cabecera)
    public int crearPedido(Pedido pedido); // Devuelve el ID generado

    public void actualizarEstado(int idPedido, String estado);

    public Pedido getPedidoEnCurso(int idUsuario); // Busca si hay un carrito abierto

    public void cerrarPedido(Pedido pedido); // Pasa estado a 'f' y actualiza importes

    // Métodos para las Líneas (Detalle) - A veces se hacen en un ILineasPedidoDAO separado, 
    // pero para simplificar se suelen agrupar aquí si están muy acoplados.
    public void agregarLinea(LineaPedido linea);

    public void eliminarLinea(int idLinea);

    public void vaciarLineas(int idPedido);

    public List<LineaPedido> getLineasPedido(int idPedido);

    public void close();
    public int registrarPedido(Pedido carrito);
    public java.util.List<Pedido> getPedidosUsuario(int idUsuario);
}
