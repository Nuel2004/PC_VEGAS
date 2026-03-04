<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<header class="topbar" style="justify-content: space-between;">

    <a href="${pageContext.request.contextPath}/inicio" title="Volver al inicio" style="display: flex; align-items: center;">
        <img src="${pageContext.request.contextPath}/img/logo_pcvegas.png" 
             alt="PC VEGAS Logo" title="PC VEGAS Logo"
             style="height: 50px; width: auto;">
    </a>

    <nav class="nav-links">

        <c:if test="${empty sessionScope.usuario}">
            <a href="${pageContext.request.contextPath}/registro">Registro</a>
            <a href="${pageContext.request.contextPath}/login">Inicio de Sesión</a>
        </c:if>

        <c:if test="${not empty sessionScope.usuario}">
            <div class="user-profile-nav">
                <a href="${pageContext.request.contextPath}/perfil" title="Ir a mi perfil">
                    <img src="${pageContext.request.contextPath}/img/${sessionScope.usuario.avatar}" 
                         alt="Mi Perfil" class="nav-avatar" 
                         onerror="this.src='${pageContext.request.contextPath}/img/default.jpg'">
                </a>

                <span class="user-greet">Hola, <strong>${sessionScope.usuario.nombre}</strong></span>

                <a href="${pageContext.request.contextPath}/MisPedidosController" 
                   title="Ver historial de pedidos"
                   style="margin: 0 10px; text-decoration: none; font-weight: bold; border-bottom: 1px solid transparent; transition: all 0.2s;"
                   onmouseover="this.style.borderBottom = '1px solid #333'"
                   onmouseout="this.style.borderBottom = '1px solid transparent'">
                    📦 Mis Pedidos
                </a>

                <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Cerrar Sesión</a>
            </div>
        </c:if>

        <a href="${pageContext.request.contextPath}/CarritoController" class="cart-btn">
            🛒 Carrito 
            <c:if test="${not empty sessionScope.carrito and sessionScope.carrito.totalArticulos > 0}">
                (${sessionScope.carrito.totalArticulos})
            </c:if>
        </a>
    </nav>
</header>