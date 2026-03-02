package es.pcvegas.controllers;

import es.pcvegas.beans.Usuario;
import es.pcvegas.dao.IUsuariosDAO;
import es.pcvegas.daofactory.DAOFactory;

import java.io.IOException;
import java.sql.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "LogoutController", urlPatterns = {"/LogoutController", "/logout"})
public class LogoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);

        if (sesion != null) {
            Object uObj = sesion.getAttribute("usuario");
            if (uObj instanceof Usuario) {
                Usuario u = (Usuario) uObj;

                DAOFactory daof = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
                IUsuariosDAO udao = daof.getUsuariosDAO();

                udao.actualizarUltimoAcceso(u.getIdUsuario(), new Date(System.currentTimeMillis()));
            }

            sesion.invalidate();
        }

        request.getRequestDispatcher("/inicio").forward(request, response);
    }
}