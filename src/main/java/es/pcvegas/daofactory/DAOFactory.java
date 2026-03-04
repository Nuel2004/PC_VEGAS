package es.pcvegas.daofactory;

import es.pcvegas.dao.ICategoriasDAO;
import es.pcvegas.dao.IPedidosDAO;
import es.pcvegas.dao.IProductosDAO;
import es.pcvegas.dao.IUsuariosDAO;

/**
 * Clase abstracta que define el contrato para las fábricas de DAOs (Data Access
 * Objects). Implementa el patrón de diseño Abstract Factory, permitiendo
 * cambiar la fuente de datos subyacente (MySQL, Oracle, etc.) sin modificar el
 * código de la lógica de negocio.
 *
 * * @author manuel
 */
public abstract class DAOFactory {

    /**
     * Constante identificadora para la fábrica de MySQL.
     */
    public static final int MYSQL = 1;

    // Métodos legacy o generados automáticamente (parecen estar duplicados/singular vs plural)
    /**
     * Método auxiliar (no implementado).
     *
     * @return Object
     * @deprecated Usar {@link #getProductosDAO()}
     */
    public static Object getProductoDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Método auxiliar (no implementado).
     *
     * @return Object
     * @deprecated Usar {@link #getCategoriasDAO()}
     */
    public static Object getCategoriaDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // --- MÉTODOS ABSTRACTOS ---
    /**
     * Obtiene una instancia del DAO para la gestión de usuarios.
     *
     * @return Interfaz {@link IUsuariosDAO}
     */
    public abstract IUsuariosDAO getUsuariosDAO();

    /**
     * Obtiene una instancia del DAO para la gestión de productos.
     *
     * @return Interfaz {@link IProductosDAO}
     */
    public abstract IProductosDAO getProductosDAO();

    /**
     * Obtiene una instancia del DAO para la gestión de categorías.
     *
     * @return Interfaz {@link ICategoriasDAO}
     */
    public abstract ICategoriasDAO getCategoriasDAO();

    /**
     * Obtiene una instancia del DAO para la gestión de pedidos.
     *
     * @return Interfaz {@link IPedidosDAO}
     */
    public abstract IPedidosDAO getPedidosDAO();

    /**
     * Método Factory Method estático que devuelve una fábrica concreta según el
     * tipo solicitado.
     *
     * * @param tipo Identificador del tipo de base de datos (ej.
     * {@link #MYSQL}).
     * @return Una instancia concreta de DAOFactory (ej.
     * {@link MySQLDAOFactory}) o null si el tipo no existe.
     */
    public static DAOFactory getDAOFactory(int tipo) {
        switch (tipo) {
            case MYSQL:
                return new MySQLDAOFactory();
            default:
                return null;
        }
    }
}
