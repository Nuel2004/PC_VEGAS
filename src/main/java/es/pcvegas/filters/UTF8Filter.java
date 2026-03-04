/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.pcvegas.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * Filtro que intercepta todas las peticiones para forzar la codificación de
 * caracteres a UTF-8. Esto asegura que los datos enviados en formularios
 * (tildes, ñ, etc.) se procesen correctamente.
 *
 * * @author manuel
 */
@WebFilter(filterName = "UTF8Filter", urlPatterns = {"/*"})
public class UTF8Filter implements Filter {

    /**
     * Codificación a aplicar (por defecto UTF-8).
     */
    private String encoding;

    /**
     * Inicializa el filtro leyendo la configuración. Si no se especifica
     * parámetro, usa "UTF-8" por defecto.
     *
     * * @param fConfig Configuración del filtro.
     * @throws ServletException Si ocurre un error de inicialización.
     */
    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated method stub
        encoding = fConfig.getInitParameter("requestEncoding");
        if (encoding == null) {
            encoding = "UTF-8";
        }
    }

    /**
     * Libera los recursos del filtro.
     *
     * @see Filter#destroy()
     */
    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * Aplica la codificación establecida a la petición y pasa el control al
     * siguiente elemento de la cadena.
     *
     * * @param request La solicitud servlet.
     * @param response La respuesta servlet.
     * @param chain La cadena de filtros.
     * @throws IOException Si ocurre un error de entrada/salida.
     * @throws ServletException Si ocurre un error en el servlet.
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding(encoding);

        // pass the request along the filter chain
        chain.doFilter(request, response);
    }

}
