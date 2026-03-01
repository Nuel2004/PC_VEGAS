package es.pcvegas.beans;

import java.io.Serializable;

public class Categoria implements Serializable {

    private int idCategoria;
    private String nombre;
    private String imagen;

    // Constructor vacío (Obligatorio para BeanUtils)
    public Categoria() {
    }

    // Getters y Setters
    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
