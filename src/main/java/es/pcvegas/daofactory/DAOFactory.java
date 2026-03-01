package es.pcvegas.daofactory;

import es.pcvegas.dao.ICategoriasDAO;
import es.pcvegas.dao.IPedidosDAO; 
import es.pcvegas.dao.IProductosDAO;
import es.pcvegas.dao.IUsuariosDAO;

public abstract class DAOFactory {

    public static final int MYSQL = 1;

    public static Object getProductoDAO() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static Object getCategoriaDAO() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public abstract IUsuariosDAO getUsuariosDAO();

    public abstract IProductosDAO getProductosDAO();

    public abstract ICategoriasDAO getCategoriasDAO();

    public abstract IPedidosDAO getPedidosDAO(); // <--- NUEVO

    public static DAOFactory getDAOFactory(int tipo) {
        switch (tipo) {
            case MYSQL:
                return new MySQLDAOFactory();
            default:
                return null;
        }
    }
}
