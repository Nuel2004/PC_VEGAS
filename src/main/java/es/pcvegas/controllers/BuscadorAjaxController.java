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

@WebServlet(name = "BuscadorAjaxController", urlPatterns = {"/BuscadorAjaxController"})
public class BuscadorAjaxController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

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
