<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page isErrorPage="true"%> <%-- IMPORTANTE: Permite acceder a la excepción --%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Error 404 - PC VEGAS</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
        <style>
            .error-container {
                text-align: center;
                padding: 60px 20px;
                max-width: 800px;
                margin: 0 auto;
                min-height: 50vh;
            }
            .icon-error {
                font-size: 80px;
                margin-bottom: 20px;
                display: block;
            }
            .btn-retry {
                display: inline-block;
                padding: 12px 25px;
                background-color: #e74c3c;
                color: white;
                text-decoration: none;
                border-radius: 5px;
                font-weight: bold;
                transition: background 0.3s;
            }
            .btn-retry:hover {
                background-color: #c0392b;
            }
            
            /* Estilos para el bloque de detalles técnicos (oculto por defecto) */
            .tech-details-box {
                margin-top: 40px;
                background: #fff0f0;
                color: #721c24;
                padding: 20px;
                border: 1px solid #f5c6cb;
                border-radius: 5px;
                text-align: left;
                font-family: 'Consolas', 'Monaco', monospace;
                font-size: 0.9em;
                overflow-x: auto;
                display: none; /* OCULTO */
                box-shadow: inset 0 0 10px rgba(0,0,0,0.05);
            }
            .btn-toggle-details {
                background: none;
                border: none;
                color: #999;
                text-decoration: underline;
                cursor: pointer;
                font-size: 0.85em;
                margin-top: 30px;
            }
            .btn-toggle-details:hover {
                color: #555;
            }
        </style>
    </head>
    <body>

        <jsp:include page="/jsp/fragmentos/_header.jsp" />

        <main class="error-container">
            <div class="icon-error">💥</div>
            <h1 style="color: #333;">¡Vaya! Algo salió mal.</h1>
            <p style="font-size: 1.1em; color: #666; margin-bottom: 30px;">
                Hemos tenido un problema técnico interno al procesar tu solicitud.
                <br>Por favor, inténtalo de nuevo en unos minutos.
            </p>

            <a href="${pageContext.request.contextPath}/inicio" class="btn-retry">
                🔄 Volver al inicio
            </a>

        </main>

        <jsp:include page="/jsp/fragmentos/_footer.jsp" />

    </body>
</html>