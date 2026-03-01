package es.pcvegas.dao;

import es.pcvegas.beans.Producto;
import java.util.List;

public interface IProductosDAO {

    public List<Producto> getProductos();

    public List<Producto> getProductosPorCategoria(int idCategoria);

    public List<Producto> getProductosOferta(); // Para la portada (ej: últimos 5)

    public Producto getProductoDetalle(int idProducto);

    public List<Producto> getProductosAleatorios();

    // Obtiene una lista de todas las marcas sin repetir
    java.util.List<String> getMarcas();

    // Obtiene un array de 2 posiciones: [0] = precio más barato, [1] = precio más caro
    double[] getRangoPrecios();

    public List<Producto> filtrarProductos(String busqueda, String[] marcas, double precioMin, double precioMax, Integer idCategoria);
}
