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
import javax.servlet.http.Part;
import org.apache.commons.beanutils.BeanUtils;

@WebServlet(name = "RegistroController", urlPatterns = {"/RegistroController", "/registro"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 5, // 5 MB
        maxRequestSize = 1024 * 1024 * 10 // 10 MB
)
public class RegistroController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Simplemente mostramos el formulario
        request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // -----------------------------------------------------------
        // 1. VALIDACIÓN AJAX (Para comprobar si el email existe en vivo)
        // -----------------------------------------------------------
        String accion = request.getParameter("accion");
        if ("comprobarEmail".equals(accion)) {
            String email = request.getParameter("email");
            DAOFactory daof = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
            IUsuariosDAO udao = daof.getUsuariosDAO();
            Usuario u = udao.getUsuarioPorEmail(email);

            response.setContentType("text/plain");
            response.getWriter().write(u != null ? "OCUPADO" : "LIBRE");
            return; // Cortamos aquí, no seguimos registrando
        }

        // -----------------------------------------------------------
        // 2. PROCESO DE REGISTRO ESTÁNDAR
        // -----------------------------------------------------------
        int idGenerado = -1;
        DAOFactory daof = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        IUsuariosDAO udao = daof.getUsuariosDAO();

        try {
            Usuario nuevoUsuario = new Usuario();

            // A) Carga automática de datos (nombre, apellidos, email, telefono, direccion...)
            // Nota: BeanUtils ignorará 'password_confirm' porque no existe en la clase Usuario
            BeanUtils.populate(nuevoUsuario, request.getParameterMap());

            // B) VALIDACIÓN DE CONTRASEÑAS (Lo que pediste)
            String pass1 = nuevoUsuario.getPassword();
            String pass2 = request.getParameter("password_confirm");

            if (pass1 == null || pass2 == null || !pass1.equals(pass2)) {
                request.setAttribute("error", "Las contraseñas no coinciden. Por favor, inténtalo de nuevo.");
                // Devolvemos el usuario al JSP para no borrarle todo lo que escribió
                request.setAttribute("usuarioPre", nuevoUsuario);
                request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
                return;
            }

            // C) CORRECCIÓN DEL NIF (Añadir letra)
            String nifNumeros = nuevoUsuario.getNif(); // BeanUtils cargó solo los números
            if (nifNumeros != null && nifNumeros.matches("\\d{8}")) {
                String nifCompleto = Utilitis.calcularNifCompleto(nifNumeros);
                nuevoUsuario.setNif(nifCompleto);
            }

            // D) SEGURIDAD Y VALORES POR DEFECTO
            nuevoUsuario.setPassword(Utilitis.getMD5(pass1)); // Encriptamos
            nuevoUsuario.setAvatar("default.jpg"); // Imagen provisional

            // E) INSERTAR EN BASE DE DATOS
            idGenerado = udao.registrar(nuevoUsuario);

            // F) GESTIÓN DE LA IMAGEN (Solo si se registró correctamente)
            if (idGenerado != -1) {

                Part filePart = request.getPart("ficheroAvatar");

                if (filePart != null && filePart.getSize() > 0 && filePart.getSubmittedFileName().length() > 0) {

                    // 1. Obtener extensión (.jpg, .png)
                    String nombreOriginal = filePart.getSubmittedFileName();
                    String extension = ".jpg"; // Por defecto
                    if (nombreOriginal.contains(".")) {
                        extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
                    }

                    // 2. Crear nombre único: avatar_ID.ext
                    String nombreArchivo = "avatar_" + idGenerado + extension;
                    String rutaDirectorio = getServletContext().getRealPath("/img");

                    // 3. Guardar en disco
                    boolean imagenGuardada = Utilitis.guardarImagen(filePart, rutaDirectorio, nombreArchivo);

                    // 4. Actualizar BD con el nombre real
                    if (imagenGuardada) {
                        udao.actualizarAvatar(idGenerado, nombreArchivo);
                    } else {
                        // Si falla al guardar la imagen, lanzamos excepción para provocar el rollback manual
                        throw new IOException("No se pudo guardar la imagen en el servidor.");
                    }
                }

                // --- ÉXITO TOTAL ---
                response.sendRedirect(request.getContextPath() + "/login");

            } else {
                // Falló el INSERT (probablemente email duplicado no detectado por JS)
                request.setAttribute("error", "Error al registrar: El email o el NIF ya existen.");
                request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();

            // --- ROLLBACK MANUAL (Requisito del profesor) ---
            // Si el usuario se creó en BD pero falló algo después (ej: la imagen), lo borramos.
            if (idGenerado != -1) {
                udao.eliminar(idGenerado);
            }

            request.setAttribute("error", "Error técnico durante el registro: " + e.getMessage());
            request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
        }
    }
}
