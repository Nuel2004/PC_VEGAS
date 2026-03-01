package es.pcvegas.controllers;

import es.pcvegas.beans.Usuario;
import es.pcvegas.dao.IUsuariosDAO;
import es.pcvegas.daofactory.DAOFactory;
import es.pcvegas.models.Utilitis;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "LoginController", urlPatterns = {"/LoginController", "/login"})
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession();
        String email = request.getParameter("email");

        // CASO 1: Si ya está logueado, mandamos al inicio
        if (sesion.getAttribute("usuario") != null) {
            request.getRequestDispatcher("/inicio").forward(request, response);
            return;
        }

        // CASO 2: Si viene el email, es que están intentando loguearse (PROCESAR FORMULARIO)
        if (email != null) {
            String passwordPlana = request.getParameter("password");
            String passwordMD5 = Utilitis.getMD5(passwordPlana);

            DAOFactory daof = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
            IUsuariosDAO udao = daof.getUsuariosDAO();
            Usuario usuarioValidado = udao.login(email, passwordMD5);

            if (usuarioValidado != null) {
                // Login correcto
                sesion.setAttribute("usuario", usuarioValidado);
                request.getRequestDispatcher("/inicio").forward(request, response);
                Utilitis.borrarCookieCarrito(response);
            } else {
                // Login incorrecto
                request.setAttribute("error", "Email o contraseña incorrectos.");
                request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            }
        } // CASO 3: Si no hay email, es que quieren ver la página (VER FORMULARIO)
        else {
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        }
    }
}
