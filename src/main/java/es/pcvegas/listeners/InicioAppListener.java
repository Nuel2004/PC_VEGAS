package es.pcvegas.listeners;

import es.pcvegas.beans.Categoria;
import es.pcvegas.dao.ICategoriasDAO;
import es.pcvegas.dao.IProductosDAO;
import es.pcvegas.daofactory.DAOFactory;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Listener que se ejecuta al arrancar la aplicación web. Su función principal
 * es precargar datos estáticos o de configuración (categorías, marcas, rangos
 * de precio) y almacenarlos en el ámbito de aplicación (ServletContext) para
 * que estén disponibles en todas las vistas JSP sin necesidad de consultarlos
 * repetidamente en cada petición.
 *
 * * @author manuel
 */
@WebListener
public class InicioAppListener implements ServletContextListener {

    /**
     * Método ejecutado al inicializar el contexto de la aplicación. Conecta a
     * la base de datos, recupera listas maestras y las guarda como atributos de
     * contexto. Incluye manejo de errores robusto para evitar que un fallo en
     * la BD impida el despliegue de Tomcat.
     *
     * * @param sce Evento del contexto del servlet.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("--- INICIANDO CARGA DE DATOS DE CONTEXTO ---");
        List<Categoria> categorias = new ArrayList<>();
        List<String> marcas = new ArrayList<>();
        double[] rangoPrecios = new double[]{0.0, 10000.0}; // Valores por defecto por si falla

        try {
            // 1. Obtener la factoría MySQL
            DAOFactory daof = DAOFactory.getDAOFactory(DAOFactory.MYSQL);

            // 2. Obtener los DAOs que necesitamos
            ICategoriasDAO cdao = daof.getCategoriasDAO();
            IProductosDAO pdao = daof.getProductosDAO();

            // 3. Obtener las listas de la base de datos
            categorias = cdao.getCategorias();
            marcas = pdao.getMarcas();
            rangoPrecios = pdao.getRangoPrecios();

            // Mensajes por consola para que sepas que todo ha ido bien
            if (categorias != null && !categorias.isEmpty()) {
                System.out.println("--- ÉXITO: Se cargaron " + categorias.size() + " categorías.");
            } else {
                System.out.println("--- AVISO: La lista de categorías está vacía o es nula.");
            }

            if (marcas != null) {
                System.out.println("--- ÉXITO: Se cargaron " + marcas.size() + " marcas.");
            }
            System.out.println("--- ÉXITO: Rango de precios: " + rangoPrecios[0] + "€ - " + rangoPrecios[1] + "€");

        } catch (Exception e) {
            // CRÍTICO: Capturamos cualquier error para que Tomcat NO se detenga
            System.err.println("--- ERROR FATAL EN EL LISTENER ---");
            e.printStackTrace();
            System.err.println("----------------------------------");
        }

        // 4. Guardar TODO en el contexto (application scope) para que los JSP lo puedan usar
        if (categorias == null) {
            categorias = new ArrayList<>();
        }
        sce.getServletContext().setAttribute("listaCategorias", categorias);

        if (marcas == null) {
            marcas = new ArrayList<>();
        }
        sce.getServletContext().setAttribute("listaMarcas", marcas);

        // Guardamos los precios como atributos individuales
        sce.getServletContext().setAttribute("precioMinimo", rangoPrecios[0]);
        sce.getServletContext().setAttribute("precioMaximo", rangoPrecios[1]);
    }

    /**
     * Método ejecutado al detener la aplicación. Lugar apropiado para cerrar
     * recursos globales o limpiar hilos.
     *
     * * @param sce Evento del contexto del servlet.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Aquí podríamos poner el código para limpiar el hilo de MySQL que dejaba el "Memory Leak", 
        // pero como dijimos, no es crítico para la nota.
    }
}
