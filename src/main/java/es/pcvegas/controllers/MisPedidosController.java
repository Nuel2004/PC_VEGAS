package es.pcvegas.controllers;

import es.pcvegas.beans.Pedido;
import es.pcvegas.beans.Usuario;
import es.pcvegas.dao.IPedidosDAO;
import es.pcvegas.daofactory.DAOFactory;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Controlador que muestra el historial de pedidos de un usuario registrado.
 * Requiere que el usuario tenga una sesión activa.
 *
 * * @author manuel
 */
@WebServlet(name = "MisPedidosController", urlPatterns = {"/MisPedidosController", "/mispedidos"})
public class MisPedidosController extends HttpServlet {

    /**
     * Recupera y muestra la lista de pedidos del usuario actual. 1. Verifica si
     * hay usuario en sesión; si no, redirige al login. 2. Consulta al DAO para
     * obtener los pedidos asociados al ID del usuario. 3. Envía la lista a la
     * vista JSP.
     *
     * * @param request La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException Si ocurre un error de E/S.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            request.getRequestDispatcher("/login").forward(request, response);
            return;
        }

        DAOFactory daof = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        IPedidosDAO pedidosDAO = daof.getPedidosDAO();

        List<Pedido> misPedidos = pedidosDAO.getPedidosUsuario(usuario.getIdUsuario());

        request.setAttribute("listaPedidos", misPedidos);

        request.getRequestDispatcher("/jsp/mis_pedidos.jsp").forward(request, response);
    }
}
