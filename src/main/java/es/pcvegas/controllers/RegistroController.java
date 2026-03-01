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
        request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // ====================================================================
        //  NUEVO: BLOQUE PARA GESTIONAR PETICIONES AJAX (Comprobar Email)
        // ====================================================================
        String tipoAccion = request.getParameter("accion");

        if ("comprobarEmail".equals(tipoAccion)) {
            String emailCheck = request.getParameter("email");
            response.setContentType("text/plain"); // Respondemos texto plano, no HTML

            if (emailCheck != null && !emailCheck.isEmpty()) {
                DAOFactory daof = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
                IUsuariosDAO udao = daof.getUsuariosDAO();

                if (udao.getUsuarioPorEmail(emailCheck) != null) {
                    response.getWriter().write("OCUPADO");
                } else {
                    response.getWriter().write("LIBRE");
                }
            }
            return; // ¡IMPORTANTE! Cortamos aquí para que no siga con el registro
        }
        // ====================================================================

        // 1. Lógica normal de REGISTRO (Si no es una comprobación AJAX)
        String pass = request.getParameter("password");
        String passRepetida = request.getParameter("password_repetida");
        String email = request.getParameter("email");

        if (pass == null || !pass.equals(passRepetida)) {
            request.setAttribute("error", "Las contraseñas no coinciden.");
            request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
            return;
        }

        DAOFactory daof = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        IUsuariosDAO udao = daof.getUsuariosDAO();

        // Doble comprobación de seguridad (Backend)
        if (udao.getUsuarioPorEmail(email) != null) {
            request.setAttribute("error", "Ese email ya está registrado.");
            request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
            return;
        }

        Usuario nuevoUsuario = new Usuario();

        try {
            BeanUtils.populate(nuevoUsuario, request.getParameterMap());
        } catch (Exception e) {
            e.printStackTrace();
        }

        nuevoUsuario.setPassword(Utilitis.getMD5(pass));

        String nifNumeros = request.getParameter("nif_numeros");
        String nifCalculado = Utilitis.calcularNifCompleto(nifNumeros);

        if (nifCalculado != null) {
            nuevoUsuario.setNif(nifCalculado);
        } else {
            request.setAttribute("error", "El NIF debe tener 8 números.");
            request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
            return;
        }

        nuevoUsuario.setAvatar("default.jpg");

        int idGenerado = -1;

        try {
            boolean registroOk = udao.registrar(nuevoUsuario);

            if (registroOk) {
                Usuario uGuardado = udao.getUsuarioPorEmail(nuevoUsuario.getEmail());
                idGenerado = uGuardado.getIdUsuario();

                Part filePart = request.getPart("ficheroAvatar");

                if (filePart != null && filePart.getSize() > 0) {
                    String nombreArchivo = "avatar_" + idGenerado + ".jpg";
                    String rutaDirectorio = getServletContext().getRealPath("/img");

                    boolean imagenGuardada = Utilitis.guardarImagen(filePart, rutaDirectorio, nombreArchivo);

                    if (imagenGuardada) {
                        udao.actualizarAvatar(idGenerado, nombreArchivo);
                    } else {
                        throw new IOException("Fallo al escribir en disco");
                    }
                }

                // Registro exitoso -> Al login
                response.sendRedirect(request.getContextPath() + "/login");

            } else {
                request.setAttribute("error", "Error al insertar en base de datos.");
                request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (idGenerado != -1) {
                udao.eliminar(idGenerado);
            }
            request.setAttribute("error", "Error en el registro. Inténtelo de nuevo.");
            request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
        }
    }
}
