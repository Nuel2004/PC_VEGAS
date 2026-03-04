package es.pcvegas.dao;

import es.pcvegas.beans.Usuario;
import java.sql.Date;

/**
 * Interfaz para la gestión de Usuarios. Incluye autenticación, registro y
 * actualización de perfiles.
 *
 * * @author manuel
 */
public interface IUsuariosDAO {

    /**
     * Valida las credenciales de un usuario.
     *
     * * @param email Correo electrónico.
     * @param passwordMD5 Contraseña encriptada en MD5.
     * @return Objeto Usuario si las credenciales son correctas, o null.
     */
    Usuario login(String email, String passwordMD5);

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * * @param u Objeto Usuario con los datos.
     * @return ID del usuario generado.
     */
    int registrar(Usuario u);

    /**
     * Elimina un usuario por su ID.
     *
     * * @param idusuario ID a eliminar.
     */
    void eliminar(int idusuario);

    /**
     * Actualiza la fecha de último acceso del usuario.
     *
     * * @param idusuario ID del usuario.
     * @param fecha Fecha y hora del acceso.
     */
    void actualizarUltimoAcceso(int idusuario, Date fecha);

    // --- MÉTODOS QUE FALTABAN Y CAUSABAN ERROR ---
    /**
     * Busca un usuario por su email. Útil para verificar disponibilidad en el
     * registro (AJAX).
     *
     * * @param email Email a buscar.
     * @return Usuario encontrado o null.
     */
    Usuario getUsuarioPorEmail(String email);

    /**
     * Actualiza solo el nombre del archivo de avatar del usuario.
     *
     * * @param idUsuario ID del usuario.
     * @param nombreArchivo Nuevo nombre de archivo.
     * @return true si se actualizó correctamente.
     */
    boolean actualizarAvatar(int idUsuario, String nombreArchivo);

    /**
     * Actualiza los datos personales del perfil de usuario.
     *
     * * @param u Objeto usuario con los nuevos datos.
     * @param nuevoAvatar Nombre del nuevo avatar (o null si no cambia).
     * @return true si se actualizó correctamente.
     */
    boolean actualizarPerfil(Usuario u, String nuevoAvatar);
}
