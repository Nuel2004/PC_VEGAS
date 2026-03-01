<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>¡Pedido Completado! - PC VEGAS</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
        <style>
            .thank-you-container {
                text-align: center;
                padding: 60px 30px;
                background-color: #fff;
                border-radius: 10px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
                max-width: 600px;
                margin: 80px auto;
                border-top: 5px solid #2ecc71;
            }
            .thank-you-icon {
                font-size: 80px;
                margin-bottom: 20px;
            }
            .thank-you-container h1 {
                color: #333;
                margin-bottom: 15px;
            }
            .thank-you-container p {
                color: #666;
                font-size: 1.1em;
                line-height: 1.6;
                margin-bottom: 30px;
            }
            .btn-volver {
                display: inline-block;
                padding: 15px 30px;
                background-color: #e74c3c;
                color: white;
                text-decoration: none;
                font-weight: bold;
                border-radius: 5px;
                transition: background 0.3s;
                font-size: 1.1em;
            }
            .btn-volver:hover {
                background-color: #c0392b;
            }
        </style>
    </head>
    <body style="background-color: #f4f4f9;">

        <jsp:include page="/jsp/fragmentos/_header.jsp" />

        <main>
            <div class="thank-you-container">
                <div class="thank-you-icon">✅</div>
                <h1>¡Gracias por tu compra, ${sessionScope.usuario.nombre}!</h1>
                <p>
                    Tu pedido ha sido procesado correctamente y tu carrito ha sido vaciado.
                    <br><br>
                    En breve comenzaremos a preparar tus productos para el envío.
                    ¡Esperamos volver a verte pronto!
                </p>
                <a href="${pageContext.request.contextPath}/inicio" class="btn-volver">🏠 Volver a la Tienda</a>
            </div>
        </main>
        <jsp:include page="/jsp/fragmentos/_footer.jsp" />
    </body>
</html>