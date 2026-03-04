package es.pcvegas.beans;

import java.io.Serializable;

/**
 * Representa un artículo o producto a la venta en la tienda. Contiene
 * información descriptiva, precio y su categorización.
 *
 * * @author manuel
 */
public class Producto implements Serializable {

    /**
     * Identificador único del producto.
     */
    private int idProducto;

    /**
     * Identificador de la categoría a la que pertenece el producto.
     */
    private int idCategoria;

    /**
     * Nombre comercial del producto.
     */
    private String nombre;

    /**
     * Descripción detallada del producto.
     */
    private String descripcion;

    /**
     * Precio unitario del producto.
     */
    private double precio;

    /**
     * Marca o fabricante del producto.
     */
    private String marca;

    /**
     * Ruta o nombre del archivo de imagen del producto.
     */
    private String imagen;

    /**
     * * Objeto Categoria asociado. Atributo auxiliar para mostrar el nombre de
     * la categoría en listados de productos sin necesidad de múltiples
     * consultas a la base de datos.
     */
    private Categoria categoriaObj;

    /**
     * Constructor de la clase. Inicializa la imagen con un valor por defecto
     * ("default.jpg").
     */
    public Producto() {
        this.imagen = "default.jpg";
    }

    /**
     * Obtiene el identificador del producto.
     *
     * * @return El ID del producto.
     */
    public int getIdProducto() {
        return idProducto;
    }

    /**
     * Establece el identificador del producto.
     *
     * * @param idProducto El ID a establecer.
     */
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    /**
     * Obtiene el identificador de la categoría del producto.
     *
     * * @return El ID de la categoría.
     */
    public int getIdCategoria() {
        return idCategoria;
    }

    /**
     * Establece el identificador de la categoría del producto.
     *
     * * @param idCategoria El ID de la categoría.
     */
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     * Obtiene el nombre del producto.
     *
     * * @return El nombre del producto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del producto.
     *
     * * @param nombre El nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la descripción del producto.
     *
     * * @return La descripción.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción del producto.
     *
     * * @param descripcion La descripción a establecer.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el precio unitario del producto.
     *
     * * @return El precio.
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio del producto.
     *
     * * @param precio El precio a establecer.
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Obtiene la marca del producto.
     *
     * * @return La marca.
     */
    public String getMarca() {
        return marca;
    }

    /**
     * Establece la marca del producto.
     *
     * * @param marca La marca a establecer.
     */
    public void setMarca(String marca) {
        this.marca = marca;
    }

    /**
     * Obtiene la ruta de la imagen del producto.
     *
     * * @return La imagen.
     */
    public String getImagen() {
        return imagen;
    }

    /**
     * Establece la imagen del producto.
     *
     * * @param imagen La ruta de la imagen.
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    /**
     * Obtiene el objeto Categoria asociado para uso en la vista.
     *
     * * @return El objeto Categoria.
     */
    public Categoria getCategoriaObj() {
        return categoriaObj;
    }

    /**
     * Establece el objeto Categoria asociado.
     *
     * * @param categoriaObj El objeto Categoria a asociar.
     */
    public void setCategoriaObj(Categoria categoriaObj) {
        this.categoriaObj = categoriaObj;
    }
}
