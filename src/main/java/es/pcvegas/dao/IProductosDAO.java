package es.pcvegas.dao;

import es.pcvegas.beans.Producto;
import java.util.List;

/**
 * Interfaz que define las operaciones de acceso a datos para la entidad
 * Producto. Incluye métodos de búsqueda, filtrado y obtención de detalles.
 *
 * * @author manuel
 */
public interface IProductosDAO {

    /**
     * Obtiene todos los productos de la base de datos.
     *
     * * @return Lista completa de productos.
     */
    public List<Producto> getProductos();

    /**
     * Obtiene los productos pertenecientes a una categoría específica.
     *
     * * @param idCategoria ID de la categoría a filtrar.
     * @return Lista de productos de dicha categoría.
     */
    public List<Producto> getProductosPorCategoria(int idCategoria);

    /**
     * Obtiene una lista de productos destacados u ofertados. Útil para mostrar
     * en la página de inicio.
     *
     * * @return Lista de productos en oferta.
     */
    public List<Producto> getProductosOferta();

    /**
     * Obtiene la información detallada de un producto específico.
     *
     * * @param idProducto ID del producto a buscar.
     * @return Objeto Producto si existe, o null si no se encuentra.
     */
    public Producto getProductoDetalle(int idProducto);

    /**
     * Obtiene una selección aleatoria de productos. Utilizado para mostrar
     * variedad en el escaparate.
     *
     * * @return Lista de productos aleatorios.
     */
    public List<Producto> getProductosAleatorios();

    /**
     * Obtiene una lista de todas las marcas únicas disponibles en el catálogo.
     *
     * * @return Lista de nombres de marcas (Strings).
     */
    java.util.List<String> getMarcas();

    /**
     * Calcula el rango de precios actual del catálogo.
     *
     * * @return Un array de double donde [0] es el precio mínimo y [1] el
     * máximo.
     */
    double[] getRangoPrecios();

    /**
     * Realiza una búsqueda avanzada de productos aplicando múltiples filtros.
     *
     * * @param busqueda Texto a buscar en el nombre del producto (puede ser
     * null).
     * @param marcas Array de marcas a filtrar (puede ser null).
     * @param precioMin Precio mínimo del rango.
     * @param precioMax Precio máximo del rango.
     * @param idCategoria ID de la categoría (puede ser null para buscar en
     * todas).
     * @return Lista de productos que cumplen con todos los criterios.
     */
    public List<Producto> filtrarProductos(String busqueda, String[] marcas, double precioMin, double precioMax, Integer idCategoria);
}
