package es.pcvegas.controllers;

import es.pcvegas.dao.IUsuariosDAO;
import es.pcvegas.daofactory.DAOFactory;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controlador auxiliar AJAX para validaciones rápidas de usuario.
 * Principalmente utilizado para verificar si un email ya está en uso durante el
 * registro.
 *
 * * @author manuel
 */
@WebServlet(name = "UsuariosAjaxController", urlPatterns = {"/UsuariosAjaxController"})
public class UsuariosAjaxController extends HttpServlet {

    /**
     * Redirige las peticiones GET al método doPost.
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
     * Verifica la disponibilidad de un email. Devuelve texto plano "OCUPADO",
     * "LIBRE" o "VACIO".
     *
     * * @param request La solicitud HTTP con parámetro
     * 'accion'='comprobarEmail' y 'email'.
     * @param response La respuesta HTTP en texto plano.
     * @throws ServletException Error en servlet.
     * @throws IOException Error de E/S.
     */
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
