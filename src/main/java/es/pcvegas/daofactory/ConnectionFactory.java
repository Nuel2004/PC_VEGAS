package es.pcvegas.daofactory;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionFactory {

    private static DataSource dataSource = null;

    private ConnectionFactory() {
        // Constructor privado para evitar instancias
    }

    public static Connection getConnection() {
        Connection conexion = null;
        try {
            if (dataSource == null) {
                Context contextoInicial = new InitialContext();
                // Buscamos el recurso definido en META-INF/context.xml
                dataSource = (DataSource) contextoInicial.lookup("java:comp/env/jdbc/pc_vegas");
            }
            conexion = dataSource.getConnection();
        } catch (NamingException | SQLException e) {
            e.printStackTrace();
            System.out.println("Error al obtener la conexión del pool: " + e.getMessage());
        }
        return conexion;
    }

    public static void closeConnection(Connection conexion) {
        try {
            if (conexion != null) {
                conexion.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}