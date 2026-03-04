package es.pcvegas.dao;

import es.pcvegas.beans.Categoria;
import java.util.List;

/**
 * Interfaz que define las operaciones de acceso a datos para la entidad
 * Categoria.
 *
 * * @author manuel
 */
public interface ICategoriasDAO {

    /**
     * Obtiene el listado completo de categorías disponibles en la base de
     * datos.
     *
     * * @return Una lista de objetos Categoria.
     */
    public List<Categoria> getCategorias();

    /**
     * Cierra los recursos asociados al DAO si fuera necesario.
     */
    public void close();
}
