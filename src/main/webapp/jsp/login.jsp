<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Iniciar Sesión - PC VEGAS</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
    </head>
    <body>

        <jsp:include page="/jsp/fragmentos/_header.jsp" />

        <main class="auth-wrapper">
            <div class="auth-box login-size">
                <h2>Bienvenido</h2>
                <c:if test="${not empty error}">
                    <div class="alert-error">${error}</div>
                </c:if>
                <form action="${pageContext.request.contextPath}/LoginController" method="post">
                    <div class="form-group">
                        <label for="email">Correo Electrónico</label>
                        <input type="email" id="email" name="email" required placeholder="tu@email.com">
                    </div>
                    <div class="form-group">
                        <label for="password">Contraseña</label>
                        <input type="password" id="password" name="password" required placeholder="••••••••">
                    </div>
                    <button type="submit" class="btn-submit">Entrar</button>
                </form>
                <div class="auth-links">
                    ¿No tienes cuenta? <a href="${pageContext.request.contextPath}/registro">Regístrate aquí</a>
                </div>
            </div>
        </main>
        <jsp:include page="/jsp/fragmentos/_footer.jsp" />
    </body>
</html>