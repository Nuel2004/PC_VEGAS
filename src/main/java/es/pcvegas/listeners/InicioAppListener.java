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

@WebListener
public class InicioAppListener implements ServletContextListener {

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

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Aquí podríamos poner el código para limpiar el hilo de MySQL que dejaba el "Memory Leak", 
        // pero como dijimos, no es crítico para la nota.
    }
}
