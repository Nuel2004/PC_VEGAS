package es.pcvegas.controllers;

import es.pcvegas.beans.LineaPedido;
import es.pcvegas.beans.Pedido;
import es.pcvegas.beans.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Controlador que gestiona las operaciones del carrito de la compra mediante
 * peticiones asíncronas (AJAX). Permite incrementar o decrementar cantidades y
 * devuelve el estado actualizado del carrito en formato JSON.
 *
 * * @author manuel
 */
@WebServlet(name = "CarritoAjaxController", urlPatterns = {"/CarritoAjaxController"})
public class CarritoAjaxController extends HttpServlet {

    /**
     * Redirige las peticiones GET al método doPost.
     *
     * * @param request La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error específico del servlet.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * Procesa la modificación de cantidades del carrito vía AJAX. 1. Valida los
     * parámetros (operación e ID de producto). 2. Localiza la línea de pedido
     * correspondiente en la sesión. 3. Actualiza la cantidad (sumar o restar) y
     * recalcula subtotales. 4. Si el usuario es anónimo, actualiza también la
     * cookie del carrito. 5. Devuelve un objeto JSON con los nuevos totales y
     * el estado de la línea.
     *
     * * @param request La solicitud HTTP con los parámetros 'operacion' y
     * 'idProducto'.
     * @param response La respuesta HTTP en formato application/json.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException Si ocurre un error de escritura en la respuesta.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // 1) Validar parámetros
        String operacion = request.getParameter("operacion");
        String idStr = request.getParameter("idProducto");

        if (operacion == null || operacion.isBlank() || idStr == null || idStr.isBlank()) {
            out.print("{\"ok\":false,\"msg\":\"Parámetros inválidos\"}");
            out.flush();
            return;
        }

        int idProducto;
        try {
            idProducto = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            out.print("{\"ok\":false,\"msg\":\"ID de producto inválido\"}");
            out.flush();
            return;
        }

        HttpSession session = request.getSession();
        Pedido carrito = (Pedido) session.getAttribute("carrito");

        if (carrito == null || carrito.getLineas() == null) {
            out.print("{\"ok\":false,\"msg\":\"No hay carrito\"}");
            out.flush();
            return;
        }

        int nuevaCantidad = 0;
        double nuevoSubtotalLinea = 0.0;
        boolean eliminado = false;

        // 2) Modificar cantidad
        Iterator<LineaPedido> it = carrito.getLineas().iterator();
        while (it.hasNext()) {
            LineaPedido linea = it.next();
            if (linea == null) {
                continue;
            }

            // Comparar por idProducto directo (evita NPE por productoObj null)
            if (linea.getIdProducto() == idProducto
                    || (linea.getProductoObj() != null && linea.getProductoObj().getIdProducto() == idProducto)) {

                if ("mas".equalsIgnoreCase(operacion)) {
                    linea.setCantidad(linea.getCantidad() + 1);
                } else if ("menos".equalsIgnoreCase(operacion)) {
                    linea.setCantidad(linea.getCantidad() - 1);
                    if (linea.getCantidad() <= 0) {
                        it.remove();
                        eliminado = true;
                    }
                }

                if (!eliminado) {
                    nuevaCantidad = linea.getCantidad();
                    if (linea.getProductoObj() != null) {
                        nuevoSubtotalLinea = linea.getCantidad() * linea.getProductoObj().getPrecio();
                    } else {
                        // Si no hay productoObj no podemos calcular bien (evita NPE)
                        nuevoSubtotalLinea = 0.0;
                    }
                }
                break;
            }
        }

        // 3) Recalcular total carrito (base imponible + iva)
        double subtotalCarrito = 0.0;
        for (LineaPedido linea : carrito.getLineas()) {
            if (linea != null && linea.getProductoObj() != null) {
                subtotalCarrito += linea.getProductoObj().getPrecio() * linea.getCantidad();
            }
        }
        carrito.setImporte(subtotalCarrito);
        carrito.setIva(subtotalCarrito * 0.21);

        // 4) Guardar cookie si anónimo
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");
        if (usuarioLogueado == null) {
            es.pcvegas.models.Utilitis.guardarCarritoEnCookie(response, carrito);
        }

        // 5) JSON respuesta
        String json = String.format(java.util.Locale.US,
                "{\"ok\":true,\"eliminado\":%b,\"cantidad\":%d,\"lineaSubtotal\":%.2f,\"total\":%.2f,\"iva\":%.2f,\"totalLineas\":%d}",
                eliminado, nuevaCantidad, nuevoSubtotalLinea, carrito.getImporte(), carrito.getIva(), carrito.getLineas().size()
        );

        out.print(json);
        out.flush();
    }
}
