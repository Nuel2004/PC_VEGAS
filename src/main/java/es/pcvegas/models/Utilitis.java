package es.pcvegas.models;

import es.pcvegas.beans.LineaPedido;
import es.pcvegas.beans.Pedido;
import es.pcvegas.beans.Usuario;
import java.io.File;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 * Clase de utilidades generales de la aplicación.
 *
 * @author manuel
 */
public class Utilitis {

    private static final String COOKIE_CARRITO = "carrito_temporal";
    private static final int COOKIE_MAX_AGE = 2 * 24 * 60 * 60;

    public static void recalcularTotales(Pedido carrito) {
        if (carrito == null || carrito.getLineas() == null) {
            return;
        }

        double subtotal = 0.0;
        for (LineaPedido linea : carrito.getLineas()) {
            if (linea != null && linea.getProductoObj() != null) {
                subtotal += linea.getProductoObj().getPrecio() * linea.getCantidad();
            }
        }
        carrito.setImporte(subtotal);
        carrito.setIva(subtotal * 0.21);
    }

    public static void sincronizarCarritoSiAnonimo(HttpSession session, HttpServletResponse response, Pedido carrito) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            guardarCarritoEnCookie(response, carrito);
        }
    }

    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al encriptar con MD5", e);
        }
    }

    public static String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return "desconocido.jpg";
    }

    public static void guardarCarritoEnCookie(HttpServletResponse response, Pedido carrito) {
        if (response == null) {
            return;
        }
        if (carrito == null || carrito.getLineas() == null || carrito.getLineas().isEmpty()) {
            borrarCookieCarrito(response);
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (LineaPedido linea : carrito.getLineas()) {
            int id = (linea.getProductoObj() != null) ? linea.getProductoObj().getIdProducto() : linea.getIdProducto();
            if (id > 0 && linea.getCantidad() > 0) {
                if (sb.length() > 0) {
                    sb.append("|");
                }
                sb.append(id).append("-").append(linea.getCantidad());
            }
        }

        try {
            String value = URLEncoder.encode(sb.toString(), StandardCharsets.UTF_8.toString());
            Cookie cookie = new Cookie(COOKIE_CARRITO, value);
            cookie.setPath("/");
            cookie.setMaxAge(COOKIE_MAX_AGE);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        } catch (Exception e) {
        }
    }

    public static void borrarCookieCarrito(HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE_CARRITO, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static Map<Integer, Integer> leerCookieCarrito(HttpServletRequest request) {
        Map<Integer, Integer> mapa = new HashMap<>();
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if (COOKIE_CARRITO.equals(c.getName())) {
                    try {
                        String decoded = URLDecoder.decode(c.getValue(), StandardCharsets.UTF_8.toString());
                        for (String item : decoded.split("\\|")) {
                            String[] p = item.split("-");
                            if (p.length == 2) {
                                mapa.put(Integer.parseInt(p[0]), Integer.parseInt(p[1]));
                            }
                        }
                    } catch (Exception e) {
                    }
                    break;
                }
            }
        }
        return mapa;
    }

    public static String calcularNifCompleto(String numeros) {
        if (numeros == null || !numeros.matches("[0-9]{8}")) {
            return null;
        }
        String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
        return numeros + letras.charAt(Integer.parseInt(numeros) % 23);
    }

    public static boolean guardarImagen(Part filePart, String ruta, String nombre) {
        try {
            File folder = new File(ruta);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            try (InputStream input = filePart.getInputStream()) {
                Files.copy(input, new File(folder, nombre).toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
