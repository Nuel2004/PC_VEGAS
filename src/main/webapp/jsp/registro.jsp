<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Registro de Usuario - PC VEGAS</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">    
    </head>
    <body>

        <jsp:include page="/jsp/fragmentos/_header.jsp" />

        <main class="auth-wrapper">
            <div class="auth-box">
                <h2>Crear una Cuenta</h2>

                <form action="${pageContext.request.contextPath}/RegistroController" method="post" enctype="multipart/form-data" class="form-grid">

                    <c:if test="${not empty error}">
                        <div class="alert-error">${error}</div>
                    </c:if>

                    <div class="form-group full-width">
                        <label for="email">Correo Electrónico *</label>
                        <input type="email" id="email" name="email" required maxlength="50" placeholder="tu@email.com">
                    </div>

                    <div class="form-group">
                        <label for="password">Contraseña *</label>
                        <input type="password" id="password" name="password" required maxlength="100" placeholder="••••••••">
                    </div>

                    <div class="form-group">
                        <label for="password_repetida">Repetir Contraseña *</label>
                        <input type="password" id="password_repetida" name="password_repetida" required maxlength="100" placeholder="••••••••">
                    </div>

                    <div class="form-group">
                        <label for="nombre">Nombre *</label>
                        <input type="text" id="nombre" name="nombre" required maxlength="20" placeholder="Tu nombre">
                    </div>

                    <div class="form-group">
                        <label for="apellidos">Apellidos *</label>
                        <input type="text" id="apellidos" name="apellidos" required maxlength="30" placeholder="Tus apellidos">
                    </div>

                    <div class="form-group">
                        <label for="nif_numeros">NIF / DNI *</label>
                        <div style="display: flex; gap: 10px;">
                            <input type="text" id="nif_numeros" name="nif_numeros" required maxlength="8" pattern="[0-9]{8}" title="Introduce los 8 números de tu DNI" placeholder="12345678" style="flex: 3;"> 
                            <input type="text" id="nif_letra" name="nif_letra" readonly style="flex: 1; text-align: center; font-weight: bold; background-color: #e9ecef; cursor: not-allowed;" placeholder="-">
                        </div>
                        <small style="color: #666; font-size: 0.8em; margin-top: 5px; display: block;">Escribe los números y calcularemos la letra.</small>
                    </div>

                    <div class="form-group">
                        <label for="telefono">Teléfono</label>
                        <input type="tel" id="telefono" name="telefono" maxlength="9" pattern="[0-9]{9}" title="Debe contener 9 números" placeholder="600123456">
                    </div>

                    <div class="form-group full-width">
                        <label for="direccion">Dirección Completa *</label>
                        <input type="text" id="direccion" name="direccion" required maxlength="40">
                    </div>

                    <div class="form-group">
                        <label for="codigo_postal">Código Postal *</label>
                        <input type="text" id="codigo_postal" name="codigo_postal" required maxlength="5" pattern="[0-9]{5}" title="Debe contener 5 números" placeholder="28001">
                    </div>

                    <div class="form-group">
                        <label for="localidad">Localidad *</label>
                        <input type="text" id="localidad" name="localidad" required maxlength="40" placeholder="Ciudad o municipio">
                    </div>

                    <div class="form-group">
                        <label for="provincia">Provincia *</label>
                        <input type="text" id="provincia" name="provincia" required maxlength="30" placeholder="Provincia">
                    </div>

                    <div class="form-group full-width" style="margin-top: 20px; text-align: center; border-top: 1px solid #eee; padding-top: 20px;">
                        <label>Foto de Perfil (Opcional)</label>

                        <div style="margin: 10px auto; width: 100px; height: 100px; border-radius: 50%; overflow: hidden; border: 3px solid #ddd; background: #f9f9f9; display: flex; align-items: center; justify-content: center;">
                            <img id="imgPreview" src="${pageContext.request.contextPath}/img/default.jpg" alt="Tu Foto" style="width: 100%; height: 100%; object-fit: cover;">
                        </div>

                        <input type="file" id="ficheroAvatar" name="ficheroAvatar" accept="image/*" style="display: none;">

                        <button type="button" id="btnSelectImage" style="background: #3498db; color: white; border: none; padding: 8px 15px; border-radius: 4px; cursor: pointer; margin-right: 10px;">
                            📂 Seleccionar Imagen
                        </button>
                        <span id="nombreArchivo" style="color: #666; font-size: 0.9em;">Ningún archivo seleccionado</span>
                    </div>

                    <button type="submit" class="btn-submit">Crear Cuenta</button>

                    <div class="auth-links">
                        ¿Ya tienes una cuenta? <a href="${pageContext.request.contextPath}/login">Inicia Sesión aquí</a>
                    </div>
                </form>
            </div>
        </main>

        <jsp:include page="/jsp/fragmentos/_footer.jsp" />

        <script src="${pageContext.request.contextPath}/js/validacion.js"></script>
        <script src="${pageContext.request.contextPath}/js/autocalcular_dni.js"></script>
        <script src="${pageContext.request.contextPath}/js/preview_avatar.js"></script>

    </body>
</html>