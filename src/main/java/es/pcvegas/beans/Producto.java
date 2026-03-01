package es.pcvegas.beans;

import java.io.Serializable;

public class Producto implements Serializable {

    private int idProducto;
    private int idCategoria; // Relación con categoría
    private String nombre;
    private String descripcion;
    private double precio;
    private String marca;
    private String imagen;

    // Atributo auxiliar para mostrar el nombre de la categoría en los listados si fuera necesario
    // No está en la tabla productos, pero es útil en la vista.
    private Categoria categoriaObj;

    public Producto() {
        this.imagen = "default.jpg"; // Valor por defecto del enunciado
    }

    // Getters y Setters
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Categoria getCategoriaObj() {
        return categoriaObj;
    }

    public void setCategoriaObj(Categoria categoriaObj) {
        this.categoriaObj = categoriaObj;
    }
}
