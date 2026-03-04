package es.pcvegas.dao;

import es.pcvegas.beans.Pedido;
import es.pcvegas.beans.LineaPedido;
import java.util.List;

/**
 * Interfaz para la gestión de Pedidos y Líneas de Pedido. Agrupa operaciones
 * tanto de cabecera como de detalle.
 *
 * * @author manuel
 */
public interface IPedidosDAO {

    /**
     * Crea un nuevo pedido en base de datos.
     *
     * * @param pedido Objeto Pedido a insertar.
     * @return El ID autogenerado del pedido.
     */
    public int crearPedido(Pedido pedido);

    /**
     * Actualiza el estado de un pedido (ej: 'c' a 'f').
     *
     * * @param idPedido ID del pedido.
     * @param estado Nuevo estado ('c', 'f', etc.).
     */
    public void actualizarEstado(int idPedido, String estado);

    /**
     * Busca si existe un pedido en curso (carrito) para un usuario.
     *
     * * @param idUsuario ID del usuario.
     * @return Objeto Pedido con estado 'c', o null si no existe.
     */
    public Pedido getPedidoEnCurso(int idUsuario);

    /**
     * Finaliza un pedido, actualizando su estado a 'f' y guardando importes
     * finales.
     *
     * * @param pedido Objeto Pedido con los datos a cerrar.
     */
    public void cerrarPedido(Pedido pedido);

    // Métodos para las Líneas (Detalle)
    /**
     * Agrega una línea de detalle a un pedido existente.
     *
     * * @param linea Objeto LineaPedido a insertar.
     */
    public void agregarLinea(LineaPedido linea);

    /**
     * Elimina una línea específica de un pedido.
     *
     * * @param idLinea ID de la línea a eliminar.
     */
    public void eliminarLinea(int idLinea);

    /**
     * Elimina todas las líneas asociadas a un pedido (vaciar carrito).
     *
     * * @param idPedido ID del pedido.
     */
    public void vaciarLineas(int idPedido);

    /**
     * Recupera todas las líneas asociadas a un pedido. Incluye datos del
     * producto asociado mediante JOIN.
     *
     * * @param idPedido ID del pedido.
     * @return Lista de líneas de pedido.
     */
    public List<LineaPedido> getLineasPedido(int idPedido);

    /**
     * Cierra recursos del DAO.
     */
    public void close();

    /**
     * Registra un pedido completo (cabecera + líneas) en una única transacción.
     *
     * * @param carrito Objeto Pedido con sus líneas pobladas.
     * @return ID del pedido generado o -1 en caso de error.
     */
    public int registrarPedido(Pedido carrito);

    /**
     * Obtiene el historial de pedidos de un usuario.
     *
     * * @param idUsuario ID del usuario.
     * @return Lista de pedidos históricos.
     */
    public java.util.List<Pedido> getPedidosUsuario(int idUsuario);
}
