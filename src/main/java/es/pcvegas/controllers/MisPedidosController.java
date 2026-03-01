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

@WebServlet(name = "MisPedidosController", urlPatterns = {"/MisPedidosController", "/mispedidos"})
public class MisPedidosController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Seguridad: Si no hay usuario, al login
        if (usuario == null) {
            request.getRequestDispatcher("/login").forward(request, response);
            return;
        }

        // Recuperamos los pedidos
        DAOFactory daof = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        IPedidosDAO pedidosDAO = daof.getPedidosDAO();
        
        List<Pedido> misPedidos = pedidosDAO.getPedidosUsuario(usuario.getIdUsuario());
        
        request.setAttribute("listaPedidos", misPedidos);
        
        request.getRequestDispatcher("/jsp/mis_pedidos.jsp").forward(request, response);
    }
}