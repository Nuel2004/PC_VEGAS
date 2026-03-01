package es.pcvegas.dao;

import es.pcvegas.beans.Usuario;

public interface IUsuariosDAO {

    public boolean registrar(Usuario usuario);

    public Usuario getUsuarioPorEmail(String email);

    public Usuario login(String email, String password);

    public void close();

    public void actualizarAvatar(int idUsuario, String avatar);

    public void eliminar(int idUsuario);

    public boolean actualizarPerfil(es.pcvegas.beans.Usuario u);
    // Cambia la firma para que acepte el avatar también

    public boolean actualizarPerfil(es.pcvegas.beans.Usuario u, String nuevoAvatarFilename);
}
