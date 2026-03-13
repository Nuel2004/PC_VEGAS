package es.pcvegas.controllers;

import es.pcvegas.beans.LineaPedido;
import es.pcvegas.beans.Pedido;
import es.pcvegas.beans.Producto;
import es.pcvegas.dao.IProductosDAO;
import es.pcvegas.daofactory.DAOFactory;
import es.pcvegas.models.Utilitis;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "CarritoAjaxController", urlPatterns = {"/CarritoAjaxController"})
public class CarritoAjaxController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {

            String operacion = request.getParameter("operacion");
            String idStr = request.getParameter("idProducto");

            if (operacion == null || operacion.trim().isEmpty() || idStr == null || idStr.trim().isEmpty()) {
                out.print("{\"ok\":false,\"msg\":\"Parámetros inválidos\"}");
                return;
            }

            int idProducto;
            try {
                idProducto = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                out.print("{\"ok\":false,\"msg\":\"ID inválido\"}");
                return;
            }

            HttpSession session = request.getSession();
            Pedido carrito = (Pedido) session.getAttribute("carrito");
            if (carrito == null) {
                carrito = new Pedido();
                session.setAttribute("carrito", carrito);
            }
            if ("add".equalsIgnoreCase(operacion)) {
                boolean existe = false;
                for (LineaPedido linea : carrito.getLineas()) {
                    if (linea.getProductoObj() != null && linea.getProductoObj().getIdProducto() == idProducto) {
                        linea.setCantidad(linea.getCantidad() + 1);
                        existe = true;
                        break;
                    } else if (linea.getIdProducto() == idProducto) {
                        linea.setCantidad(linea.getCantidad() + 1);
                        existe = true;
                        break;
                    }
                }

                if (!existe) {
                    IProductosDAO pdao = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getProductosDAO();
                    Producto p = pdao.getProductoDetalle(idProducto);
                    if (p != null) {
                        LineaPedido linea = new LineaPedido();
                        linea.setIdProducto(idProducto);
                        linea.setCantidad(1);
                        linea.setProductoObj(p);
                        carrito.getLineas().add(linea);
                    }
                }

                Utilitis.recalcularTotales(carrito);
                Utilitis.sincronizarCarritoSiAnonimo(session, response, carrito);

                out.print("{\"ok\":true, \"totalArticulos\": " + carrito.getTotalArticulos() + "}");
                out.flush(); 
                return;
            }

            int nuevaCantidad = 0;
            double nuevoSubtotalLinea = 0.0;
            boolean eliminado = false;

            Iterator<LineaPedido> it = carrito.getLineas().iterator();
            while (it.hasNext()) {
                LineaPedido linea = it.next();
                if ((linea.getProductoObj() != null && linea.getProductoObj().getIdProducto() == idProducto)
                        || linea.getIdProducto() == idProducto) {

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
                        }
                    }
                    break;
                }
            }

            Utilitis.recalcularTotales(carrito);
            Utilitis.sincronizarCarritoSiAnonimo(session, response, carrito);

            String json = String.format(Locale.US,
                    "{\"ok\":true,\"eliminado\":%b,\"cantidad\":%d,\"lineaSubtotal\":%.2f,\"total\":%.2f,\"iva\":%.2f,\"totalLineas\":%d, \"totalArticulos\":%d}",
                    eliminado, nuevaCantidad, nuevoSubtotalLinea, carrito.getImporte(), carrito.getIva(), carrito.getLineas().size(), carrito.getTotalArticulos()
            );
            out.print(json);
        }
    }
}
