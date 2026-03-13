package es.pcvegas.daofactory;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Clase utilitaria encargada de gestionar las conexiones a la base de datos.
 * Utiliza un DataSource obtenido mediante JNDI (Java Naming and Directory
 * Interface) para aprovechar el pool de conexiones del servidor de
 * aplicaciones.
 *
 * * @author manuel
 */
public class ConnectionFactory {

    /**
     * Fuente de datos (Pool de conexiones) cacheada tras la primera llamada.
     */
    private static DataSource dataSource = null;

    /**
     * Constructor privado para evitar la instanciación de la clase. Al ser una
     * clase de utilidad con métodos estáticos, no debe ser instanciada.
     */
    private ConnectionFactory() {
    }

    /**
     * Obtiene una conexión activa a la base de datos desde el pool. Si es la
     * primera vez que se llama, busca el DataSource en el contexto JNDI.
     *
     * * @return Un objeto {@link Connection} listo para usar, o null si ocurre
     * un error.
     */
    public static Connection getConnection() {
        Connection conexion = null;
        try {
            if (dataSource == null) {
                Context contextoInicial = new InitialContext();
                dataSource = (DataSource) contextoInicial.lookup("java:comp/env/jdbc/pc_vegas");
            }
            conexion = dataSource.getConnection();
        } catch (NamingException | SQLException e) {
            e.printStackTrace();
            System.out.println("Error al obtener la conexión del pool: " + e.getMessage());
        }
        return conexion;
    }

    /**
     * Cierra la conexión proporcionada de manera segura. En realidad, devuelve
     * la conexión al pool para que pueda ser reutilizada, no la cierra
     * físicamente a nivel de socket.
     *
     * * @param conexion La conexión a cerrar/liberar.
     */
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
