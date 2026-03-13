package es.pcvegas.controllers;

import es.pcvegas.beans.Usuario;
import es.pcvegas.dao.IUsuariosDAO;
import es.pcvegas.daofactory.DAOFactory;
import es.pcvegas.models.Utilitis;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@WebServlet(name = "PerfilController", urlPatterns = {"/PerfilController", "/perfil"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 15 // 15 MB
)
public class PerfilController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        request.getRequestDispatcher("/jsp/perfil.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");

        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String nif = request.getParameter("nif");
        String telefono = request.getParameter("telefono");
        String direccion = request.getParameter("direccion");
        String cp = request.getParameter("codigoPostal");
        String localidad = request.getParameter("localidad");
        String provincia = request.getParameter("provincia");

        String error = null;

        if (nombre == null || nombre.trim().isEmpty()) {
            error = "El nombre es obligatorio.";
        } else if (apellidos == null || apellidos.trim().isEmpty()) {
            error = "Los apellidos son obligatorios.";
        } else if (nif == null || nif.trim().isEmpty()) {
            error = "El NIF es obligatorio.";
        } else if (telefono == null || telefono.trim().isEmpty()) {
            error = "El teléfono es obligatorio.";
        } else if (direccion == null || direccion.trim().isEmpty()) {
            error = "La dirección es obligatoria.";
        }

        if (error != null) {
            request.setAttribute("mensajeError", error);
            request.getRequestDispatcher("/jsp/perfil.jsp").forward(request, response);
            return;
        }

        Part filePart = request.getPart("ficheroAvatar");
        String avatarFilename = null;

        if (filePart != null && filePart.getSize() > 0 && filePart.getSubmittedFileName().length() > 0) {
            String extension = ".jpg";
            if (filePart.getSubmittedFileName().contains(".")) {
                extension = filePart.getSubmittedFileName().substring(filePart.getSubmittedFileName().lastIndexOf("."));
            }
            avatarFilename = "avatar_" + usuarioSesion.getIdUsuario() + "_" + System.currentTimeMillis() + extension;
            String rutaReal = getServletContext().getRealPath("/img");

            boolean subidaCorrecta = Utilitis.guardarImagen(filePart, rutaReal, avatarFilename);
            if (!subidaCorrecta) {
                avatarFilename = null;
            }
        }

        usuarioSesion.setNombre(nombre.trim());
        usuarioSesion.setApellidos(apellidos.trim());
        usuarioSesion.setNif(nif.trim());
        usuarioSesion.setTelefono(telefono.trim());
        usuarioSesion.setDireccion(direccion.trim());
        usuarioSesion.setCodigoPostal(cp != null ? cp.trim() : "");
        usuarioSesion.setLocalidad(localidad != null ? localidad.trim() : "");
        usuarioSesion.setProvincia(provincia != null ? provincia.trim() : "");

        DAOFactory daof = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        IUsuariosDAO udao = daof.getUsuariosDAO();

        boolean exito = udao.actualizarPerfil(usuarioSesion, avatarFilename);

        if (exito) {
            if (avatarFilename != null) {
                usuarioSesion.setAvatar(avatarFilename);
            }
            session.setAttribute("usuario", usuarioSesion);
            request.setAttribute("mensajeExito", "Perfil actualizado correctamente.");
        } else {
            request.setAttribute("mensajeError", "No se pudieron guardar los cambios en la base de datos.");
        }

        request.getRequestDispatcher("/jsp/perfil.jsp").forward(request, response);
    }
}
