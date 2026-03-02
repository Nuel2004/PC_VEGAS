package es.pcvegas.controllers;

import es.pcvegas.dao.IUsuariosDAO;
import es.pcvegas.daofactory.DAOFactory;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "UsuariosAjaxController", urlPatterns = {"/UsuariosAjaxController"})
public class UsuariosAjaxController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (!"comprobarEmail".equals(accion)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String email = request.getParameter("email");
        response.setContentType("text/plain;charset=UTF-8");

        if (email == null || email.isBlank()) {
            response.getWriter().write("VACIO");
            return;
        }

        DAOFactory daof = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        IUsuariosDAO udao = daof.getUsuariosDAO();

        response.getWriter().write(udao.getUsuarioPorEmail(email) != null ? "OCUPADO" : "LIBRE");
    }
}
