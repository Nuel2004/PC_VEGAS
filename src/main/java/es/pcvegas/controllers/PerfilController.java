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
        // Seguridad: Si no hay usuario, mandar al login
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

        // Creamos una copia para intentar actualizar, así si falla la BD no corrompemos la sesión
        // (En este caso simple, modificaremos el objeto pero con cuidado de no meter nulos)
        // 1. Recogida de parámetros de texto
        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String nif = request.getParameter("nif");
        String telefono = request.getParameter("telefono");
        String direccion = request.getParameter("direccion");
        String cp = request.getParameter("codigoPostal");
        String localidad = request.getParameter("localidad");
        String provincia = request.getParameter("provincia");

        // 2. Procesamiento de la Imagen (Avatar)
        Part filePart = request.getPart("ficheroAvatar"); // nombre del input type="file"
        String avatarFilename = null;

        if (filePart != null && filePart.getSize() > 0 && filePart.getSubmittedFileName().length() > 0) {
            // Generamos nombre único: idUsuario_timestamp.jpg
            String extension = ".jpg"; // Por simplificar asumimos jpg o extraemos extensión
            if (filePart.getSubmittedFileName().contains(".")) {
                extension = filePart.getSubmittedFileName().substring(filePart.getSubmittedFileName().lastIndexOf("."));
            }
            avatarFilename = "avatar_" + usuarioSesion.getIdUsuario() + "_" + System.currentTimeMillis() + extension;

            String rutaReal = getServletContext().getRealPath("/img");

            boolean subidaCorrecta = Utilitis.guardarImagen(filePart, rutaReal, avatarFilename);
            if (!subidaCorrecta) {
                avatarFilename = null; // Si falla, no actualizamos la foto
            }
        }

        // 3. Actualización del Objeto Usuario (SOLO SI NO SON NULL)
        // Corrección del error: Si nif viene null, NO lo tocamos.
        if (nombre != null && !nombre.isEmpty()) {
            usuarioSesion.setNombre(nombre);
        }
        if (apellidos != null && !apellidos.isEmpty()) {
            usuarioSesion.setApellidos(apellidos);
        }

        // AQUÍ ESTABA EL FALLO: Solo actualizamos NIF si viene un valor real
        if (nif != null && !nif.trim().isEmpty()) {
            usuarioSesion.setNif(nif);
        }

        if (telefono != null) {
            usuarioSesion.setTelefono(telefono);
        }
        if (direccion != null) {
            usuarioSesion.setDireccion(direccion);
        }
        if (cp != null) {
            usuarioSesion.setCodigoPostal(cp);
        }
        if (localidad != null) {
            usuarioSesion.setLocalidad(localidad);
        }
        if (provincia != null) {
            usuarioSesion.setProvincia(provincia);
        }

        // La foto solo se cambia si el usuario subió una nueva
        String avatarParaGuardar = (avatarFilename != null) ? avatarFilename : usuarioSesion.getAvatar();

        // 4. Guardar en Base de Datos
        DAOFactory daof = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        IUsuariosDAO udao = daof.getUsuariosDAO();

        boolean exito = udao.actualizarPerfil(usuarioSesion, avatarFilename); // Pasamos avatarFilename (puede ser null si no cambió)

        if (exito) {
            // Si la BD actualizó bien, confirmamos el cambio de avatar en el objeto de sesión
            if (avatarFilename != null) {
                usuarioSesion.setAvatar(avatarFilename);
            }
            session.setAttribute("usuario", usuarioSesion); // Refrescamos sesión
            request.setAttribute("mensajeExito", "Perfil actualizado correctamente.");
        } else {
            request.setAttribute("mensajeError", "No se pudieron guardar los cambios en la base de datos.");
        }

        // 5. Volver a la vista
        request.getRequestDispatcher("/jsp/perfil.jsp").forward(request, response);
    }
}
