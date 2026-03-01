package es.pcvegas.dao;

import es.pcvegas.beans.Categoria;
import java.util.List;

public interface ICategoriasDAO {

    public List<Categoria> getCategorias();

    public void close();
}
