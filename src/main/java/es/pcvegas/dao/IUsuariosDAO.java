package es.pcvegas.dao;

import es.pcvegas.beans.Usuario;
import java.sql.Date;

public interface IUsuariosDAO {

    Usuario login(String email, String passwordMD5);

    int registrar(Usuario u);

    void eliminar(int idusuario);

    void actualizarUltimoAcceso(int idusuario, Date fecha);

    // --- MÉTODOS QUE FALTABAN Y CAUSABAN ERROR ---
    
    // Usado en RegistroController para verificar si el email existe
    Usuario getUsuarioPorEmail(String email);

    // Usado en RegistroController para guardar la foto tras obtener el ID
    boolean actualizarAvatar(int idUsuario, String nombreArchivo);

    // Usado en PerfilController para guardar cambios
    boolean actualizarPerfil(Usuario u, String nuevoAvatar);
}