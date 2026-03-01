package es.pcvegas.daofactory;

import es.pcvegas.dao.CategoriasDAO;
import es.pcvegas.dao.ICategoriasDAO;
import es.pcvegas.dao.IPedidosDAO; // <--- NUEVO
import es.pcvegas.dao.IProductosDAO;
import es.pcvegas.dao.IUsuariosDAO;
import es.pcvegas.dao.PedidosDAO; // <--- NUEVO
import es.pcvegas.dao.ProductosDAO;
import es.pcvegas.dao.UsuariosDAO;

public class MySQLDAOFactory extends DAOFactory {

    @Override
    public IUsuariosDAO getUsuariosDAO() {
        return new UsuariosDAO();
    }

    @Override
    public IProductosDAO getProductosDAO() {
        return new ProductosDAO(); // Asegúrate de que IProductosDAO y ProductosDAO coincidan en plural
    }

    @Override
    public ICategoriasDAO getCategoriasDAO() {
        return new CategoriasDAO();
    }

    @Override
    public IPedidosDAO getPedidosDAO() { // <--- NUEVO
        return new PedidosDAO();
    }
}
