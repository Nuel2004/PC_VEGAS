package es.pcvegas.controllers;

import es.pcvegas.beans.LineaPedido;
import es.pcvegas.beans.Pedido;
import es.pcvegas.beans.Usuario;
import es.pcvegas.dao.IPedidosDAO;
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

/**
 * Controlador para cerrar la sesión del usuario. Guarda el carrito actual en la
 * base de datos antes de salir y actualiza la fecha de último acceso.
 *
 * * @author manuel
 */
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

                Pedido carritoSession = (Pedido) sesion.getAttribute("carrito");

                if (carritoSession != null && !carritoSession.getLineas().isEmpty()) {
                    IPedidosDAO pdao = daof.getPedidosDAO();

                    Pedido carritoBD = pdao.getPedidoEnCurso(u.getIdUsuario());
                    int idPedidoGuardar;

                    if (carritoBD != null) {
                        idPedidoGuardar = carritoBD.getIdPedido();
                        pdao.vaciarLineas(idPedidoGuardar);
                    } else {
                        carritoSession.setIdUsuario(u.getIdUsuario());
                        idPedidoGuardar = pdao.crearPedido(carritoSession);
                    }

                    for (LineaPedido linea : carritoSession.getLineas()) {
                        linea.setIdPedido(idPedidoGuardar); 
                        pdao.agregarLinea(linea);
                    }
                }

                IUsuariosDAO udao = daof.getUsuariosDAO();
                udao.actualizarUltimoAcceso(u.getIdUsuario(), new Date(System.currentTimeMillis()));
            }

            sesion.invalidate();
        }

        request.getRequestDispatcher("/inicio").forward(request, response);
    }
}
