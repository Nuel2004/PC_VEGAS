package es.pcvegas.controllers;

import es.pcvegas.beans.LineaPedido;
import es.pcvegas.beans.Pedido;
import es.pcvegas.beans.Producto;
import es.pcvegas.beans.Usuario;
import es.pcvegas.dao.IProductosDAO;
import es.pcvegas.dao.IUsuariosDAO;
import es.pcvegas.daofactory.DAOFactory;
import es.pcvegas.models.Utilitis;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "LoginController", urlPatterns = {"/LoginController", "/login"})
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession();

        if (sesion.getAttribute("usuario") != null) {
            response.sendRedirect(request.getContextPath() + "/inicio");
            return;
        }

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email != null && password != null) {
            String passwordMD5 = Utilitis.getMD5(password);

            DAOFactory daof = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
            IUsuariosDAO udao = daof.getUsuariosDAO();

            Usuario usuarioValidado = udao.login(email, passwordMD5);

            if (usuarioValidado != null) {
                // LOGIN CORRECTO
                sesion.setAttribute("usuario", usuarioValidado);

                // --- GESTIÓN DEL CARRITO (COOKIE -> SESIÓN) ---
                if (usuarioValidado.getUltimoAcceso() == null) {
                    // Usuario NUEVO: Intentamos recuperar carrito de cookie
                    Map<Integer, Integer> mapaCookie = Utilitis.leerCookieCarrito(request);

                    if (mapaCookie != null && !mapaCookie.isEmpty()) {
                        // CONVERTIR MAPA A OBJETO PEDIDO
                        Pedido carritoRecuperado = new Pedido();
                        carritoRecuperado.setIdUsuario(usuarioValidado.getIdUsuario());

                        IProductosDAO pdao = daof.getProductosDAO();
                        double subtotal = 0.0;

                        for (Map.Entry<Integer, Integer> entry : mapaCookie.entrySet()) {
                            int idProd = entry.getKey();
                            int cantidad = entry.getValue();

                            Producto p = pdao.getProductoDetalle(idProd);
                            if (p != null) {
                                LineaPedido linea = new LineaPedido();
                                linea.setIdProducto(idProd);
                                linea.setCantidad(cantidad);
                                linea.setProductoObj(p);
                                carritoRecuperado.getLineas().add(linea);

                                subtotal += (p.getPrecio() * cantidad);
                            }
                        }

                        // Recalcular importes
                        if (!carritoRecuperado.getLineas().isEmpty()) {
                            carritoRecuperado.setImporte(subtotal);
                            carritoRecuperado.setIva(subtotal * 0.21);
                            sesion.setAttribute("carrito", carritoRecuperado);
                        }
                    }
                    Utilitis.borrarCookieCarrito(response);

                } else {
                    // Usuario ANTIGUO: Se borra carrito anterior
                    Utilitis.borrarCookieCarrito(response);
                    sesion.removeAttribute("carrito");
                }

                response.sendRedirect(request.getContextPath() + "/inicio");

            } else {
                request.setAttribute("error", "Email o contraseña incorrectos.");
                request.setAttribute("emailPre", email);
                request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            }

        } else {
            request.setAttribute("error", "Por favor, rellene todos los campos.");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        }
    }
}
