/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.pcvegas.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.Part;

public class Utilitis {

    private static final String COOKIE_CARRITO = "carrito_temporal";
    private static final int COOKIE_MAX_AGE = 2 * 24 * 60 * 60;

    /**
     * Encripta una cadena de texto usando el algoritmo MD5.
     */
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

    /**
     * Extrae el nombre real del archivo desde un objeto Part (subida de
     * ficheros).
     */
    public static String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return "desconocido.jpg";
    }
    /**
     * Convierte el carrito en un String (id-cant|id-cant) y lo guarda en una
     * Cookie por 2 días.
     */
    public static void guardarCarritoEnCookie(javax.servlet.http.HttpServletResponse response,
            es.pcvegas.beans.Pedido carrito) {

        if (response == null) {
            return;
        }

        if (carrito == null || carrito.getLineas() == null || carrito.getLineas().isEmpty()) {
            // Si el carrito está vacío, borramos la cookie
            borrarCookieCarrito(response);
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (es.pcvegas.beans.LineaPedido linea : carrito.getLineas()) {
            if (linea == null) {
                continue;
            }

            // IMPORTANTE: si productoObj es null (carrito desde cookie), usamos idProducto directo
            int idProducto = linea.getIdProducto();
            if (idProducto <= 0 && linea.getProductoObj() != null) {
                idProducto = linea.getProductoObj().getIdProducto();
            }

            int cantidad = linea.getCantidad();

            if (idProducto <= 0 || cantidad <= 0) {
                continue;
            }

            if (sb.length() > 0) {
                sb.append("|");
            }
            sb.append(idProducto).append("-").append(cantidad);
        }

        // Si al final no queda nada válido, borramos cookie
        if (sb.length() == 0) {
            borrarCookieCarrito(response);
            return;
        }

        // Codificamos por seguridad (aunque el formato es simple, evita problemas)
        String value;
        try {
            value = java.net.URLEncoder.encode(sb.toString(), java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            value = sb.toString();
        }

        javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(COOKIE_CARRITO, value);
        cookie.setPath("/"); // toda la app
        cookie.setMaxAge(COOKIE_MAX_AGE); // 2 días
        cookie.setHttpOnly(true); // buena práctica

        response.addCookie(cookie);
    }

    /**
     * Borra la cookie del carrito (se usa al comprar o al loguearse)
     */
    public static void borrarCookieCarrito(javax.servlet.http.HttpServletResponse response) {
        if (response == null) {
            return;
        }

        javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(COOKIE_CARRITO, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    /**
     * Lee la cookie y devuelve un mapa con los ID de productos y sus
     * cantidades. Formato cookie: id-cant|id-cant
     */
    public static java.util.Map<Integer, Integer> leerCookieCarrito(javax.servlet.http.HttpServletRequest request) {

        java.util.Map<Integer, Integer> mapa = new java.util.HashMap<>();
        if (request == null || request.getCookies() == null) {
            return mapa;
        }

        for (javax.servlet.http.Cookie c : request.getCookies()) {
            if (c == null) {
                continue;
            }

            if (COOKIE_CARRITO.equals(c.getName()) && c.getValue() != null && !c.getValue().isEmpty()) {

                String raw = c.getValue();
                String decoded;

                try {
                    decoded = java.net.URLDecoder.decode(raw, java.nio.charset.StandardCharsets.UTF_8);
                } catch (Exception e) {
                    decoded = raw;
                }

                String[] items = decoded.split("\\|");
                for (String item : items) {
                    if (item == null || item.isBlank()) {
                        continue;
                    }

                    String[] partes = item.split("-");
                    if (partes.length != 2) {
                        continue;
                    }

                    try {
                        int id = Integer.parseInt(partes[0].trim());
                        int cant = Integer.parseInt(partes[1].trim());

                        if (id > 0 && cant > 0) {
                            mapa.put(id, cant);
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
                break; // ya encontrada la cookie
            }
        }

        return mapa;
    }
    /**
     * Recibe los números del DNI y devuelve el NIF completo con letra.
     */
    public static String calcularNifCompleto(String numeros) {
        if (numeros == null || !numeros.matches("[0-9]{8}")) {
            return null;
        }
        String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
        int dni = Integer.parseInt(numeros);
        char letra = letras.charAt(dni % 23);
        return numeros + letra;
    }

    /**
     * Guarda un archivo Part en el servidor y devuelve true si sale bien.
     */
    public static boolean guardarImagen(javax.servlet.http.Part filePart, String rutaDirectorio, String nombreArchivo) {
        try {
            // Creamos la carpeta si no existe
            java.io.File folder = new java.io.File(rutaDirectorio);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            
            // Ruta completa del archivo destino
            java.io.File file = new java.io.File(folder, nombreArchivo);
            
            // Copiamos los bytes
            try (java.io.InputStream input = filePart.getInputStream()) {
                java.nio.file.Files.copy(input, file.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
