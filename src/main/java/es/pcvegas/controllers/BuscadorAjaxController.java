package es.pcvegas.controllers;

import es.pcvegas.beans.Producto;
import es.pcvegas.dao.IProductosDAO;
import es.pcvegas.daofactory.DAOFactory;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controlador que gestiona la búsqueda y filtrado de productos vía AJAX.
 * Permite filtrar por texto, marca, rango de precios y categoría. Devuelve un
 * fragmento HTML (JSP parcial) con la rejilla de productos resultante.
 *
 * * @author manuel
 */
@WebServlet(name = "BuscadorAjaxController", urlPatterns = {"/BuscadorAjaxController"})
public class BuscadorAjaxController extends HttpServlet {

    /**
     * Redirige GET a POST.
     *
     * @param request La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Error en servlet.
     * @throws IOException Error de E/S.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * Procesa los filtros de búsqueda. 1. Recoge parámetros de búsqueda (texto,
     * marcas múltiples, precios, categoría). 2. Gestiona valores por defecto
     * para precios si no se especifican. 3. Invoca al DAO para filtrar la lista
     * de productos. 4. Retorna el fragmento JSP (`_gridProductos.jsp`) para ser
     * inyectado en la vista.
     *
     * * @param request La solicitud HTTP con parámetros de filtrado.
     * @param response La respuesta HTTP.
     * @throws ServletException Error en servlet.
     * @throws IOException Error de E/S.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 1) Parámetros
        String busqueda = request.getParameter("busqueda");
        if (busqueda != null) {
            busqueda = busqueda.trim();
            if (busqueda.isEmpty()) {
                busqueda = null;
            }
        }

        // MULTIPLE: si no selecciona nada -> null (equivale a todas)
        String[] marcas = request.getParameterValues("marca");
        if (marcas != null && marcas.length == 0) {
            marcas = null;
        }

        String precioMinStr = request.getParameter("precioMin");
        String precioMaxStr = request.getParameter("precioMax");
        String idCategoriaStr = request.getParameter("idCategoria");

        // 2) Defaults de precios desde applicationScope (si existe)
        double defMin = 0.0;
        double defMax = 10000.0;

        Object appMin = request.getServletContext().getAttribute("precioMinimo");
        Object appMax = request.getServletContext().getAttribute("precioMaximo");

        if (appMin instanceof Number) {
            defMin = ((Number) appMin).doubleValue();
        }
        if (appMax instanceof Number) {
            defMax = ((Number) appMax).doubleValue();
        }

        double precioMin = defMin;
        if (precioMinStr != null && !precioMinStr.isEmpty()) {
            try {
                precioMin = Double.parseDouble(precioMinStr);
            } catch (NumberFormatException ignored) {
            }
        }

        double precioMax = defMax;
        if (precioMaxStr != null && !precioMaxStr.isEmpty()) {
            try {
                precioMax = Double.parseDouble(precioMaxStr);
            } catch (NumberFormatException ignored) {
            }
        }

        // Si vienen cruzados, los ordenamos
        if (precioMin > precioMax) {
            double tmp = precioMin;
            precioMin = precioMax;
            precioMax = tmp;
        }

        Integer idCategoria = null;
        if (idCategoriaStr != null && !idCategoriaStr.isEmpty()) {
            try {
                idCategoria = Integer.parseInt(idCategoriaStr);
            } catch (NumberFormatException ignored) {
            }
        }

        // 3) DAO
        DAOFactory daof = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        IProductosDAO pdao = daof.getProductosDAO();

        // 4) Filtrar
        List<Producto> productosFiltrados = pdao.filtrarProductos(busqueda, marcas, precioMin, precioMax, idCategoria);

        // 5) Pintar fragmento
        request.setAttribute("productos", productosFiltrados);
        request.getRequestDispatcher("/jsp/fragmentos/_gridProductos.jsp").forward(request, response);
    }
}
