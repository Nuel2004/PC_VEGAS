package es.pcvegas.controllers;

import es.pcvegas.beans.LineaPedido;
import es.pcvegas.beans.Producto;
import es.pcvegas.dao.IProductosDAO;
import es.pcvegas.daofactory.DAOFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controlador de la página de inicio (Landing Page). Se encarga de: 1.
 * Recuperar el carrito almacenado en cookies si el usuario es anónimo. 2.
 * Cargar una lista de productos aleatorios para mostrar en el escaparate.
 *
 * * @author manuel
 */
@WebServlet(name = "HomeController", urlPatterns = {"/inicio"})
public class HomeController extends HttpServlet {

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
     * Ejecuta la lógica de carga inicial de la tienda. Comprueba si existen
     * cookies de carrito para usuarios no logueados y reconstruye el objeto
     * Pedido en sesión. Finalmente, obtiene productos aleatorios y redirige a
     * la vista.
     *
     * * @param request La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException Si ocurre un error de E/S.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        javax.servlet.http.HttpSession session = request.getSession();

        DAOFactory daof = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        IProductosDAO pdao = daof.getProductosDAO();

        es.pcvegas.beans.Usuario usuario = (es.pcvegas.beans.Usuario) session.getAttribute("usuario");
        es.pcvegas.beans.Pedido carrito = (es.pcvegas.beans.Pedido) session.getAttribute("carrito");

        if (usuario == null && carrito == null) {

            java.util.Map<Integer, Integer> mapa = es.pcvegas.models.Utilitis.leerCookieCarrito(request);

            if (mapa != null && !mapa.isEmpty()) {

                carrito = new es.pcvegas.beans.Pedido();
                carrito.setLineas(new ArrayList<LineaPedido>());

                for (java.util.Map.Entry<Integer, Integer> entry : mapa.entrySet()) {
                    int idProducto = entry.getKey();
                    int cantidad = entry.getValue();

                    es.pcvegas.beans.Producto p = pdao.getProductoDetalle(idProducto);
                    if (p == null) {
                        continue;
                    }

                    es.pcvegas.beans.LineaPedido linea = new es.pcvegas.beans.LineaPedido();
                    linea.setIdProducto(idProducto);
                    linea.setCantidad(cantidad);
                    linea.setProductoObj(p);

                    carrito.getLineas().add(linea);
                }

                double subtotal = 0.0;
                for (es.pcvegas.beans.LineaPedido l : carrito.getLineas()) {
                    if (l != null && l.getProductoObj() != null) {
                        subtotal += l.getProductoObj().getPrecio() * l.getCantidad();
                    }
                }
                carrito.setImporte(subtotal);
                carrito.setIva(subtotal * 0.21);

                if (!carrito.getLineas().isEmpty()) {
                    session.setAttribute("carrito", carrito);
                }
            }
        }

        List<Producto> productosAleatorios = pdao.getProductosAleatorios();

        request.setAttribute("productos", productosAleatorios);

        request.getRequestDispatcher("/jsp/tienda.jsp").forward(request, response);
    }
}
