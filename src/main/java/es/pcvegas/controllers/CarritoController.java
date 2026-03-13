package es.pcvegas.controllers;

import es.pcvegas.beans.LineaPedido;
import es.pcvegas.beans.Pedido;
import es.pcvegas.beans.Producto;
import es.pcvegas.beans.Usuario;
import es.pcvegas.dao.IPedidosDAO;
import es.pcvegas.dao.IProductosDAO;
import es.pcvegas.daofactory.DAOFactory;
import es.pcvegas.models.Utilitis;
import java.io.IOException;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Controlador principal para la gestión del carrito de compras (flujo
 * síncrono). Maneja las acciones de añadir productos, actualizar cantidades,
 * eliminar líneas y tramitar el pedido final.
 *
 * * @author manuel
 */
@WebServlet(name = "CarritoController", urlPatterns = {"/CarritoController", "/carrito"})
public class CarritoController extends HttpServlet {

    /**
     * Redirige las peticiones GET al método doPost.
     *
     * * @param request La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException Si ocurre un error de E/S.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * Procesa las distintas acciones sobre el carrito según el parámetro
     * 'accion'.
     * <ul>
     * <li><b>add:</b> Añade un producto al carrito. Si ya existe, suma la
     * cantidad.</li>
     * <li><b>actualizar:</b> Modifica la cantidad de un producto (+/-).</li>
     * <li><b>eliminar:</b> Elimina una línea completa del carrito.</li>
     * <li><b>tramitar:</b> Finaliza la compra, guarda el pedido en BD y vacía
     * el carrito.</li>
     * </ul>
     * También se encarga de sincronizar el carrito en la Cookie si el usuario
     * es anónimo.
     *
     * * @param request La solicitud HTTP con el parámetro 'accion'.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException Si ocurre un error de E/S.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        HttpSession session = request.getSession();

        Pedido carrito = (Pedido) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new Pedido();
            session.setAttribute("carrito", carrito);
        }

        DAOFactory daof = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        IProductosDAO pdao = daof.getProductosDAO();

        if ("add".equals(accion)) {
            int idProducto = Integer.parseInt(request.getParameter("idproducto"));
            String modo = request.getParameter("modo");
            int cantidad = 1;

            boolean existe = false;
            for (LineaPedido linea : carrito.getLineas()) {
                if (linea.getProductoObj().getIdProducto() == idProducto) {
                    linea.setCantidad(linea.getCantidad() + cantidad);
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                Producto p = pdao.getProductoDetalle(idProducto);
                if (p != null) {
                    LineaPedido linea = new LineaPedido();
                    linea.setIdProducto(idProducto);
                    linea.setCantidad(cantidad);
                    linea.setProductoObj(p);
                    carrito.getLineas().add(linea);
                }
            }

            recalcularTotales(carrito);

            Usuario usuario = (Usuario) session.getAttribute("usuario");
            if (usuario == null) {
                Utilitis.guardarCarritoEnCookie(response, carrito);
            }

            if ("iframe".equals(modo)) {
                response.getWriter().write("<script>parent.actualizarContadorCarrito(" + carrito.getLineas().size() + ");</script>");
                return;
            }

            request.getRequestDispatcher("/inicio").forward(request, response);

        } else if ("actualizar".equals(accion)) {
            String operacion = request.getParameter("operacion");
            int idProducto = Integer.parseInt(request.getParameter("idProducto"));

            Iterator<LineaPedido> it = carrito.getLineas().iterator();
            while (it.hasNext()) {
                LineaPedido linea = it.next();
                if (linea.getProductoObj().getIdProducto() == idProducto) {
                    if ("mas".equals(operacion)) {
                        linea.setCantidad(linea.getCantidad() + 1);
                    } else if ("menos".equals(operacion)) {
                        linea.setCantidad(linea.getCantidad() - 1);
                        if (linea.getCantidad() <= 0) {
                            it.remove();
                        }
                    }
                    break;
                }
            }

            recalcularTotales(carrito);

            Usuario usuario = (Usuario) session.getAttribute("usuario");
            if (usuario == null) {
                Utilitis.guardarCarritoEnCookie(response, carrito);
            }

            request.getRequestDispatcher("/jsp/carrito.jsp").forward(request, response);

        } else if ("eliminar".equals(accion)) {
            int idProducto = Integer.parseInt(request.getParameter("idProducto"));

            Iterator<LineaPedido> it = carrito.getLineas().iterator();
            while (it.hasNext()) {
                LineaPedido linea = it.next();
                if (linea.getProductoObj().getIdProducto() == idProducto) {
                    it.remove();
                    break;
                }
            }

            recalcularTotales(carrito);

            Usuario usuario = (Usuario) session.getAttribute("usuario");
            if (usuario == null) {
                Utilitis.guardarCarritoEnCookie(response, carrito);
            }

            request.getRequestDispatcher("/jsp/carrito.jsp").forward(request, response);

        } else if ("tramitar".equals(accion)) {

            Usuario usuario = (Usuario) session.getAttribute("usuario");

            if (usuario == null) {
                // CORREGIDO: Usamos forward
                request.getRequestDispatcher("/login").forward(request, response);
                return;
            }

            if (carrito != null && !carrito.getLineas().isEmpty()) {
                carrito.setIdUsuario(usuario.getIdUsuario());

                IPedidosDAO pedidosDAO = daof.getPedidosDAO();
                pedidosDAO.registrarPedido(carrito);

                session.removeAttribute("carrito");
                Utilitis.borrarCookieCarrito(response);

                request.getRequestDispatcher("/jsp/gracias.jsp").forward(request, response);

            } else {
                request.getRequestDispatcher("/inicio").forward(request, response);
            }
            return;

        } else {
            request.getRequestDispatcher("/jsp/carrito.jsp").forward(request, response);
        }
    }

    /**
     * Método auxiliar para recalcular el importe total y el IVA del pedido. Se
     * invoca cada vez que se modifica el contenido del carrito.
     *
     * * @param carrito El objeto Pedido a recalcular.
     */
    private void recalcularTotales(Pedido carrito) {
        double subtotal = 0.0;
        for (LineaPedido linea : carrito.getLineas()) {
            subtotal += linea.getProductoObj().getPrecio() * linea.getCantidad();
        }
        carrito.setImporte(subtotal);
        carrito.setIva(subtotal * 0.21);
    }
}
