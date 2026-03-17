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

       
        String accion = request.getParameter("accion");
        if ("comprobarEmail".equals(accion)) {
            String email = request.getParameter("email");
            if (email == null || email.trim().isEmpty()) {
                response.getWriter().write("VACIO");
                return;
            }
            DAOFactory daof = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
            IUsuariosDAO udao = daof.getUsuariosDAO();
            Usuario u = udao.getUsuarioPorEmail(email.trim());
            response.getWriter().write(u != null ? "OCUPADO" : "LIBRE");
            return;
        }

        
        request.setCharacterEncoding("UTF-8");
        int idGenerado = -1;
        DAOFactory daof = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        IUsuariosDAO udao = daof.getUsuariosDAO();

        try {
            Usuario u = new Usuario();
            BeanUtils.populate(u, request.getParameterMap());

            
            if (u.getNombre() == null || u.getNombre().trim().isEmpty()
                    || u.getApellidos() == null || u.getApellidos().trim().isEmpty()
                    || u.getPassword() == null || u.getPassword().trim().isEmpty()
                    || u.getDireccion() == null || u.getDireccion().trim().isEmpty()
                    || u.getTelefono() == null || u.getTelefono().trim().isEmpty()) {

                request.setAttribute("error", "Por favor, rellena todos los campos obligatorios.");
                request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
                return;
            }

            
            String nifNumeros = request.getParameter("nif_numeros");
            String nifLetra = request.getParameter("nif_letra");

            
            if (nifNumeros == null || nifNumeros.trim().isEmpty()) {
                request.setAttribute("error", "El número de DNI es obligatorio.");
                request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
                return;
            }

            if (nifLetra == null || nifLetra.trim().isEmpty()) {
                try {
                    String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
                    int dniNum = Integer.parseInt(nifNumeros.trim());
                    nifLetra = String.valueOf(letras.charAt(dniNum % 23)); // ¡Magia!
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "El DNI debe contener 8 números válidos.");
                    request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
                    return;
                }
            }

            
            u.setNif(nifNumeros.trim() + nifLetra.trim().toUpperCase());
            u.setNombre(u.getNombre().trim());
            u.setApellidos(u.getApellidos().trim());
            u.setDireccion(u.getDireccion().trim());
            u.setTelefono(u.getTelefono().trim());
            u.setEmail(u.getEmail().trim());
            u.setPassword(u.getPassword().trim());

            String passCifrada = Utilitis.getMD5(u.getPassword());
            u.setPassword(passCifrada);
            u.setAvatar("default.jpg"); // Avatar temporal

            idGenerado = udao.registrar(u);

            if (idGenerado > 0) {
                
                Part filePart = request.getPart("ficheroAvatar");
                if (filePart != null && filePart.getSize() > 0 && filePart.getSubmittedFileName().length() > 0) {
                    String extension = ".jpg";
                    if (filePart.getSubmittedFileName().contains(".")) {
                        extension = filePart.getSubmittedFileName().substring(filePart.getSubmittedFileName().lastIndexOf("."));
                    }
                    String nombreArchivo = u.getNif() + "_avatar" + extension;
                    String rutaDirectorio = getServletContext().getRealPath("/img");

                    if (Utilitis.guardarImagen(filePart, rutaDirectorio, nombreArchivo)) {
                        udao.actualizarAvatar(idGenerado, nombreArchivo);
                    } else {
                        throw new IOException("Fallo al guardar la imagen.");
                    }
                }

                
                response.sendRedirect(request.getContextPath() + "/login");

            } else {
                request.setAttribute("error", "Error: El email o el DNI ya están registrados.");
                request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (idGenerado != -1) {
                udao.eliminar(idGenerado); // Rollback manual
            }
            request.setAttribute("error", "Error técnico: " + e.getMessage());
            request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
        }
    }
}
