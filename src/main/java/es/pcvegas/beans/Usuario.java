package es.pcvegas.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * Representa un usuario registrado en el sistema. Contiene información de
 * autenticación, datos personales y de contacto.
 *
 * * @author manuel
 */
public class Usuario implements Serializable {

    /**
     * Identificador único del usuario.
     */
    private int idUsuario;

    /**
     * Correo electrónico, usado habitualmente para el inicio de sesión.
     */
    private String email;

    /**
     * Contraseña del usuario (cifrada o en texto plano según implementación).
     */
    private String password;

    /**
     * Nombre de pila del usuario.
     */
    private String nombre;

    /**
     * Apellidos del usuario.
     */
    private String apellidos;

    /**
     * Número de Identificación Fiscal (DNI/NIF).
     */
    private String nif;

    /**
     * Número de teléfono de contacto.
     */
    private String telefono;

    /**
     * Dirección postal del usuario.
     */
    private String direccion;

    /**
     * Código postal de la dirección. Corresponde a 'codigo_postal' en BD.
     */
    private String codigoPostal;

    /**
     * Localidad o ciudad de residencia.
     */
    private String localidad;

    /**
     * Provincia de residencia.
     */
    private String provincia;

    /**
     * Fecha y hora del último acceso al sistema. Corresponde a 'ultimo_acceso'
     * en BD.
     */
    private Date ultimoAcceso;

    /**
     * Nombre del archivo de imagen de perfil (avatar).
     */
    private String avatar;

    /**
     * Array de bytes para almacenar la imagen del avatar si se maneja como
     * BLOB.
     */
    private byte[] avatarBytes;

    /**
     * Constructor de la clase. Inicializa el avatar con una imagen por defecto
     * ("default.jpg").
     */
    public Usuario() {
        this.avatar = "default.jpg";
    }

    /**
     * Obtiene el identificador del usuario.
     *
     * * @return El ID del usuario.
     */
    public int getIdUsuario() {
        return idUsuario;
    }

    /**
     * Establece el identificador del usuario.
     *
     * * @param idUsuario El ID a establecer.
     */
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     *
     * * @return El email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del usuario.
     *
     * * @param email El email a establecer.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene la contraseña del usuario.
     *
     * * @return La contraseña.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del usuario.
     *
     * * @param password La contraseña a establecer.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene el nombre del usuario.
     *
     * * @return El nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario.
     *
     * * @param nombre El nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene los apellidos del usuario.
     *
     * * @return Los apellidos.
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Establece los apellidos del usuario.
     *
     * * @param apellidos Los apellidos a establecer.
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Obtiene el NIF del usuario.
     *
     * * @return El NIF.
     */
    public String getNif() {
        return nif;
    }

    /**
     * Establece el NIF del usuario.
     *
     * * @param nif El NIF a establecer.
     */
    public void setNif(String nif) {
        this.nif = nif;
    }

    /**
     * Obtiene el teléfono del usuario.
     *
     * * @return El teléfono.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el teléfono del usuario.
     *
     * * @param telefono El teléfono a establecer.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene la dirección del usuario.
     *
     * * @return La dirección.
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección del usuario.
     *
     * * @param direccion La dirección a establecer.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Obtiene el código postal.
     *
     * * @return El código postal.
     */
    public String getCodigoPostal() {
        return codigoPostal;
    }

    /**
     * Establece el código postal.
     *
     * * @param codigoPostal El código postal a establecer.
     */
    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    /**
     * Obtiene la localidad.
     *
     * * @return La localidad.
     */
    public String getLocalidad() {
        return localidad;
    }

    /**
     * Establece la localidad.
     *
     * * @param localidad La localidad a establecer.
     */
    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    /**
     * Obtiene la provincia.
     *
     * * @return La provincia.
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * Establece la provincia.
     *
     * * @param provincia La provincia a establecer.
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /**
     * Obtiene la fecha del último acceso.
     *
     * * @return La fecha de último acceso.
     */
    public Date getUltimoAcceso() {
        return ultimoAcceso;
    }

    /**
     * Establece la fecha del último acceso.
     *
     * * @param ultimoAcceso La fecha a establecer.
     */
    public void setUltimoAcceso(Date ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    /**
     * Obtiene el nombre del archivo del avatar.
     *
     * * @return El nombre del archivo de imagen.
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * Establece el nombre del archivo del avatar.
     *
     * * @param avatar El nombre del archivo a establecer.
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * Obtiene los bytes de la imagen del avatar.
     *
     * * @return Array de bytes de la imagen.
     */
    public byte[] getAvatarBytes() {
        return avatarBytes;
    }

    /**
     * Establece los bytes de la imagen del avatar.
     *
     * * @param avatarBytes Array de bytes a establecer.
     */
    public void setAvatarBytes(byte[] avatarBytes) {
        this.avatarBytes = avatarBytes;
    }
}
