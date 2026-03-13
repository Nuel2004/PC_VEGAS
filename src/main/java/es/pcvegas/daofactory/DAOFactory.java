package es.pcvegas.daofactory;

import es.pcvegas.dao.ICategoriasDAO;
import es.pcvegas.dao.IPedidosDAO;
import es.pcvegas.dao.IProductosDAO;
import es.pcvegas.dao.IUsuariosDAO;

/**
 * Clase abstracta que define el contrato para las fábricas de DAOs. Implementa
 * el patrón Abstract Factory.
 *
 * @author manuel
 */
public abstract class DAOFactory {

    /**
     * Identificador para la fábrica de MySQL.
     */
    public static final int MYSQL = 1;

    public abstract IUsuariosDAO getUsuariosDAO();

    public abstract IProductosDAO getProductosDAO();

    public abstract ICategoriasDAO getCategoriasDAO();

    public abstract IPedidosDAO getPedidosDAO();

    /**
     * Factory Method que devuelve la fábrica concreta solicitada.
     *
     * @param tipo Identificador de la BD (ej. DAOFactory.MYSQL)
     * @return Una instancia de la fábrica concreta o null.
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
