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

            /* Input Readonly visualmente distinto */
            .form-group input[readonly] {
                background-color: #f0f0f0;
                color: #777;
                cursor: not-allowed;
                border-color: #eee;
            }

            .btn-guardar {
                width: 100%;
                padding: 12px;
                background-color: #111;
                color: white;
                border: none;
                border-radius: 4px;
                font-size: 1em;
                cursor: pointer;
                transition: background 0.3s;
            }
            .btn-guardar:hover {
                background-color: #333;
            }

            /* Estilos Avatar */
            .profile-avatar-section {
                text-align: center;
                margin-bottom: 30px;
            }
            .avatar-big {
                width: 120px;
                height: 120px;
                border-radius: 50%;
                object-fit: cover;
                border: 4px solid #eee;
                margin-bottom: 10px;
            }

            /* Alertas Backend */
            .alert-success {
                background: #d4edda;
                color: #155724;
                padding: 10px;
                border-radius: 4px;
                margin-bottom: 20px;
                text-align: center;
            }
            .alert-error {
                background: #f8d7da;
                color: #721c24;
                padding: 10px;
                border-radius: 4px;
                margin-bottom: 20px;
                text-align: center;
            }
        </style>
    </head>
    <body>

        <jsp:include page="/jsp/fragmentos/_header.jsp" />

        <div class="profile-container">
            <h2 style="text-align: center; margin-bottom: 30px;">Editar Perfil</h2>

            <c:if test="${not empty mensajeExito}">
                <div class="alert-success">${mensajeExito}</div>
            </c:if>
            <c:if test="${not empty mensajeError}">
                <div class="alert-error">${mensajeError}</div>
            </c:if>

            <form id="formPerfil" action="${pageContext.request.contextPath}/PerfilController" method="post" enctype="multipart/form-data">

                <div class="profile-avatar-section">
                    <img id="imgPreview" src="${pageContext.request.contextPath}/img/${sessionScope.usuario.avatar}" 
                         alt="Avatar" class="avatar-big"
                         onerror="this.src='${pageContext.request.contextPath}/img/default.jpg'">
                    <br>
                    <label for="newAvatar" style="cursor: pointer; color: #3498db; font-weight: bold;">
                        📸 Cambiar foto de perfil
                    </label>
                    <input type="file" id="newAvatar" name="ficheroAvatar" accept="image/*" style="display: none;">
                    <br>
                    <small id="avisoFoto" style="display:none; color: green; margin-top: 5px;">Imagen seleccionada</small>
                </div>

                <div class="form-group">
                    <label>Correo Electrónico (No modificable)</label>
                    <input type="email" value="${sessionScope.usuario.email}" readonly>
                </div>

                <div class="form-group">
                    <label for="nombre">Nombre *</label>
                    <input type="text" id="nombre" name="nombre" value="${sessionScope.usuario.nombre}" placeholder="Tu nombre">
                </div>

                <div class="form-group">
                    <label for="apellidos">Apellidos *</label>
                    <input type="text" id="apellidos" name="apellidos" value="${sessionScope.usuario.apellidos}" placeholder="Tus apellidos">
                </div>

                <div class="form-group">
                    <label for="nif">NIF / DNI (No modificable)</label>
                    <input type="text" id="nif" name="nif" value="${sessionScope.usuario.nif}" placeholder="12345678Z" readonly>
                </div>

                <div class="form-group">
                    <label for="telefono">Teléfono *</label>
                    <input type="tel" id="telefono" name="telefono" value="${sessionScope.usuario.telefono}" placeholder="600000000">
                </div>

                <div class="form-group">
                    <label for="direccion">Dirección *</label>
                    <input type="text" id="direccion" name="direccion" value="${sessionScope.usuario.direccion}" placeholder="Calle Ejemplo, 123">
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

        <script src="${pageContext.request.contextPath}/js/preview_avatar.js"></script>

        <script src="${pageContext.request.contextPath}/js/validacion.js"></script>
    </body>
</html>