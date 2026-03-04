package es.pcvegas.beans;

import java.io.Serializable;

/**
 * Representa una categoría de productos dentro de la tienda online. Implementa
 * Serializable para permitir el paso de objetos entre capas o sesiones.
 *
 * * @author manuel
 */
public class Categoria implements Serializable {

    /**
     * Identificador único de la categoría.
     */
    private int idCategoria;

    /**
     * Nombre descriptivo de la categoría.
     */
    private String nombre;

    /**
     * Ruta o nombre del archivo de imagen asociado a la categoría.
     */
    private String imagen;

    /**
     * Constructor vacío por defecto. Necesario para la instanciación dinámica
     * por parte de frameworks o BeanUtils.
     */
    public Categoria() {
    }

    /**
     * Obtiene el identificador de la categoría.
     *
     * * @return El ID de la categoría.
     */
    public int getIdCategoria() {
        return idCategoria;
    }

    /**
     * Establece el identificador de la categoría.
     *
     * * @param idCategoria El nuevo ID a establecer.
     */
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     * Obtiene el nombre de la categoría.
     *
     * * @return El nombre de la categoría.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la categoría.
     *
     * * @param nombre El nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la ruta o nombre de la imagen de la categoría.
     *
     * * @return La imagen de la categoría.
     */
    public String getImagen() {
        return imagen;
    }

    /**
     * Establece la imagen de la categoría.
     *
     * * @param imagen La ruta o nombre de archivo de la imagen.
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
