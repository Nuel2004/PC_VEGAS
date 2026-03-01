package es.pcvegas.controllers;

import es.pcvegas.beans.Usuario;
import es.pcvegas.dao.IUsuariosDAO;
import es.pcvegas.daofactory.DAOFactory;
import es.pcvegas.models.Utilitis;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@WebServlet(name = "PerfilController", urlPatterns = {"/PerfilController", "/perfil"})
// CAMBIO 1: Anotación obligatoria para procesar ficheros
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 15 // 15MB
)
public class PerfilController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("usuario") == null) {
            request.getRequestDispatcher("/login").forward(request, response);
            return;
        }
        request.getRequestDispatcher("/jsp/perfil.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Usuario usuarioActual = (Usuario) session.getAttribute("usuario");

        if (usuarioActual == null) {
            request.getRequestDispatcher("/login").forward(request, response);
            return;
        }

        // CAMBIO 2: PROCESAR EL FICHERO
        String avatarFilename = request.getParameter("avatarActual"); // Por defecto, mantenemos el actual
        Part filePart = request.getPart("newAvatar"); // Obtenemos el fichero subido

        // Comprobamos si el usuario ha seleccionado un fichero real
        if (filePart != null && filePart.getSize() > 0) {
            try {
                // Obtenemos el nombre original del fichero
                String originalFilename = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                // Obtenemos la extensión (ej: .jpg)
                String extension = "";
                int i = originalFilename.lastIndexOf('.');
                if (i > 0) {
                    extension = originalFilename.substring(i);
                }

                // Creamos un nombre único para evitar duplicados, usando el ID del usuario
                avatarFilename = "avatar_" + usuarioActual.getIdUsuario() + "_" + System.currentTimeMillis() + extension;

                // Definimos la ruta absoluta donde guardar la imagen (en web/img/)
                String uploadFolder = getServletContext().getRealPath("/") + "img";

                // Nos aseguramos de que la carpeta existe
                File folder = new File(uploadFolder);
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                // Guardamos el fichero en disco
                File fileToSave = new File(folder, avatarFilename);
                try (InputStream fileContent = filePart.getInputStream()) {
                    Files.copy(fileContent, fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }

                // (Opcional) Borrar el avatar antiguo si no era 'default.jpg'
                String antiguoAvatar = request.getParameter("avatarActual");
                if (antiguoAvatar != null && !antiguoAvatar.isEmpty() && !antiguoAvatar.equals("default.jpg")) {
                    File fileOld = new File(folder, antiguoAvatar);
                    if (fileOld.exists()) {
                        fileOld.delete();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("mensajeError", "Hubo un problema al subir la imagen.");
                request.getRequestDispatcher("/jsp/perfil.jsp").forward(request, response);
                return;
            }
        }

        // CAMBIO 3: Recogemos el resto de datos editables
        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String nif = request.getParameter("nif");
        String telefono = request.getParameter("telefono");
        String direccion = request.getParameter("direccion");
        String cp = request.getParameter("codigoPostal");
        String localidad = request.getParameter("localidad");
        String provincia = request.getParameter("provincia");

        // 4. Actualizamos el objeto Usuario de la sesión
        usuarioActual.setNombre(nombre);
        usuarioActual.setApellidos(apellidos);
        usuarioActual.setNif(nif);
        usuarioActual.setTelefono(telefono);
        usuarioActual.setDireccion(direccion);
        usuarioActual.setCodigoPostal(cp);
        usuarioActual.setLocalidad(localidad);
        usuarioActual.setProvincia(provincia);
        // El avatar no se toca en la sesión hasta confirmar el éxito en BD, 
        // pero lo usaremos para el DAO.

        // 5. Guardamos en Base de Datos (PERSISTENCIA), incluyendo el nuevo avatar
        DAOFactory daof = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        IUsuariosDAO udao = daof.getUsuariosDAO();

        boolean resultado = udao.actualizarPerfil(usuarioActual, avatarFilename);

        if (resultado) {
            request.setAttribute("mensajeExito", "¡Datos e imagen actualizados correctamente!");
            usuarioActual.setAvatar(avatarFilename); // Confirmado el éxito, actualizamos el avatar en la sesión
            session.setAttribute("usuario", usuarioActual);
        } else {
            request.setAttribute("mensajeError", "Hubo un error al guardar los datos en la base de datos.");
        }

        // 6. Volvemos a mostrar la página con el mensaje
        request.getRequestDispatcher("/jsp/perfil.jsp").forward(request, response);
    }
}
