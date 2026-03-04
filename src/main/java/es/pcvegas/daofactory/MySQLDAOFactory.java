package es.pcvegas.daofactory;

import es.pcvegas.dao.CategoriasDAO;
import es.pcvegas.dao.ICategoriasDAO;
import es.pcvegas.dao.IPedidosDAO;
import es.pcvegas.dao.IProductosDAO;
import es.pcvegas.dao.IUsuariosDAO;
import es.pcvegas.dao.PedidosDAO;
import es.pcvegas.dao.ProductosDAO;
import es.pcvegas.dao.UsuariosDAO;

/**
 * Implementación concreta de {@link DAOFactory} para bases de datos MySQL. Se
 * encarga de instanciar y devolver las implementaciones específicas de los DAOs
 * que trabajan contra MySQL.
 *
 * * @author manuel
 */
public class MySQLDAOFactory extends DAOFactory {

    /**
     * Devuelve una nueva instancia de UsuariosDAO para MySQL.
     *
     * @return Implementación de {@link IUsuariosDAO}.
     */
    @Override
    public IUsuariosDAO getUsuariosDAO() {
        return new UsuariosDAO();
    }

    /**
     * Devuelve una nueva instancia de ProductosDAO para MySQL.
     *
     * @return Implementación de {@link IProductosDAO}.
     */
    @Override
    public IProductosDAO getProductosDAO() {
        return new ProductosDAO();
    }

    /**
     * Devuelve una nueva instancia de CategoriasDAO para MySQL.
     *
     * @return Implementación de {@link ICategoriasDAO}.
     */
    @Override
    public ICategoriasDAO getCategoriasDAO() {
        return new CategoriasDAO();
    }

    /**
     * Devuelve una nueva instancia de PedidosDAO para MySQL.
     *
     * @return Implementación de {@link IPedidosDAO}.
     */
    @Override
    public IPedidosDAO getPedidosDAO() {
        return new PedidosDAO();
    }
}
