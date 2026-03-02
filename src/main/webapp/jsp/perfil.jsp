<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Mi Perfil - PC Vegas</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
        <style>
            .profile-container {
                max-width: 600px;
                margin: 40px auto;
                background: white;
                padding: 30px;
                border-radius: 8px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            }
            .form-group {
                margin-bottom: 15px;
            }
            .form-group label {
                display: block;
                margin-bottom: 5px;
                font-weight: bold;
                color: #333;
            }
            .form-group input {
                width: 100%;
                padding: 10px;
                border: 1px solid #ddd;
                border-radius: 4px;
                box-sizing: border-box;
            }
            .form-group input[readonly] {
                background-color: #f0f0f0;
                color: #777;
                cursor: not-allowed;
                border-color: #eee;
            }
            .btn-guardar {
                width: 100%;
                padding: 12px;
                background-color: #27ae60;
                color: white;
                border: none;
                border-radius: 4px;
                font-size: 1.1em;
                cursor: pointer;
                transition: background 0.3s;
            }
            .btn-guardar:hover {
                background-color: #2ecc71;
            }
            .avatar-preview {
                text-align: center;
                margin-bottom: 20px;
            }
            .avatar-img {
                width: 120px;
                height: 120px;
                border-radius: 50%;
                object-fit: cover;
                border: 4px solid #3498db;
            }
            .alert {
                padding: 15px;
                margin-bottom: 20px;
                border-radius: 4px;
                text-align: center;
            }
            .alert-success {
                background-color: #d4edda;
                color: #155724;
                border: 1px solid #c3e6cb;
            }
            .alert-error {
                background-color: #f8d7da;
                color: #721c24;
                border: 1px solid #f5c6cb;
            }
            .label-upload {
                cursor: pointer;
                color: #3498db;
                text-decoration: underline;
                display: inline-block;
            }
            .label-upload:hover {
                color: #2980b9;
            }
        </style>
    </head>
    <body>

        <jsp:include page="/jsp/fragmentos/_header.jsp" />

        <div class="profile-container">
            <h2 style="text-align:center; color:#333;">Editar Mi Perfil</h2>

            <c:if test="${not empty mensajeExito}"><div class="alert alert-success">${mensajeExito}</div></c:if>
            <c:if test="${not empty mensajeError}"><div class="alert alert-error">${mensajeError}</div></c:if>

                <div class="avatar-preview">
                    <img id="imgPreview" src="${pageContext.request.contextPath}/img/${sessionScope.usuario.avatar}" 
                         onerror="this.src='${pageContext.request.contextPath}/img/default.jpg'" class="avatar-img">
            </div>

            <form action="${pageContext.request.contextPath}/PerfilController" method="post" enctype="multipart/form-data">

                <div class="form-group" style="text-align: center;">
                    <label for="ficheroAvatar" class="label-upload">Cambiar foto de perfil</label>
                    <input type="file" id="ficheroAvatar" name="ficheroAvatar" accept="image/*" style="display: none;">
                    <small id="avisoFoto" style="display:none; color: #27ae60; margin-top: 5px;">¡Nueva imagen seleccionada!</small>
                </div>

                <div class="form-group">
                    <label>Email (No editable)</label>
                    <input type="email" name="email" value="${sessionScope.usuario.email}" readonly title="El email no se puede cambiar por seguridad">
                </div>

                <div class="form-group">
                    <label>NIF / DNI (No editable)</label>
                    <input type="text" id="nif" name="nif" value="${sessionScope.usuario.nif}" readonly title="El NIF no se puede modificar">
                </div>

                <div class="form-group">
                    <label for="nombre">Nombre</label>
                    <input type="text" id="nombre" name="nombre" value="${sessionScope.usuario.nombre}" required>
                </div>

                <div class="form-group">
                    <label for="apellidos">Apellidos</label>
                    <input type="text" id="apellidos" name="apellidos" value="${sessionScope.usuario.apellidos}" required>
                </div>

                <div class="form-group">
                    <label for="telefono">Teléfono</label>
                    <input type="text" id="telefono" name="telefono" value="${sessionScope.usuario.telefono}">
                </div>

                <div class="form-group">
                    <label for="direccion">Dirección</label>
                    <input type="text" id="direccion" name="direccion" value="${sessionScope.usuario.direccion}">
                </div>

                <div class="form-group">
                    <label for="codigoPostal">Código Postal</label>
                    <input type="text" id="codigoPostal" name="codigoPostal" value="${sessionScope.usuario.codigoPostal}">
                </div>

                <div class="form-group">
                    <label for="localidad">Localidad</label>
                    <input type="text" id="localidad" name="localidad" value="${sessionScope.usuario.localidad}">
                </div>

                <div class="form-group">
                    <label for="provincia">Provincia</label>
                    <input type="text" id="provincia" name="provincia" value="${sessionScope.usuario.provincia}">
                </div>

                <input type="hidden" name="avatarActual" value="${sessionScope.usuario.avatar}">

                <button type="submit" class="btn-guardar">Guardar Cambios</button>
            </form>
        </div>

        <jsp:include page="/jsp/fragmentos/_footer.jsp" />

        <script src="${pageContext.request.contextPath}/js/validacion.js"></script>
        <script src="${pageContext.request.contextPath}/js/preview_avatar.js"></script>

    </body>
</html>