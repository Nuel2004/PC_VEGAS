<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Mis Pedidos - PC Vegas</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/carrito.css">
        <style>
            /* Pequeños ajustes extra para diferenciar pedidos */
            .pedido-contenedor {
                background: #fff;
                border: 1px solid #ddd;
                border-radius: 8px;
                margin-bottom: 30px;
                padding: 20px;
                box-shadow: 0 2px 5px rgba(0,0,0,0.05);
            }
            .pedido-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                border-bottom: 2px solid #eee;
                padding-bottom: 10px;
                margin-bottom: 15px;
                background-color: #f9f9f9;
                padding: 15px;
                border-radius: 5px;
            }
            .estado-badge {
                padding: 5px 10px;
                border-radius: 15px;
                font-size: 0.9em;
                font-weight: bold;
                text-transform: uppercase;
            }
            .estado-f { background-color: #d4edda; color: #155724; } /* Finalizado (Verde) */
            .estado-c { background-color: #fff3cd; color: #856404; } /* En Curso (Amarillo) */
            .estado-x { background-color: #f8d7da; color: #721c24; } /* Cancelado (Rojo) */
        </style>
    </head>
    <body>

        <jsp:include page="/jsp/fragmentos/_header.jsp" />

        <div class="carrito-container" style="display: block; max-width: 900px; margin: 40px auto;">
            
            <h1 style="text-align:center; margin-bottom: 30px;">Historial de Pedidos</h1>

            <c:choose>
                <c:when test="${empty listaPedidos}">
                    <div style="text-align:center; padding: 50px; background:white; border-radius:8px;">
                        <h3>No has realizado ningún pedido todavía.</h3>
                        <a href="${pageContext.request.contextPath}/inicio" class="btn-comprar" style="display:inline-block; margin-top:20px; text-decoration:none;">Ir a comprar</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${listaPedidos}" var="pedido">
                        
                        <div class="pedido-contenedor">
                            <div class="pedido-header">
                                <div>
                                    <h3 style="margin:0;">Pedido #${pedido.idPedido}</h3>
                                    <span style="color:#777; font-size:0.9em;">Fecha: ${pedido.fecha}</span>
                                </div>
                                <div style="text-align:right;">
                                    <span class="estado-badge estado-${pedido.estado}">
                                        ${pedido.estado == 'f' ? 'COMPLETADO' : (pedido.estado == 'c' ? 'PENDIENTE' : pedido.estado)}
                                    </span>
                                    <div style="font-size: 1.2em; font-weight:bold; margin-top:5px;">
                                        Total: <fmt:formatNumber value="${pedido.importe + pedido.iva}" type="currency" currencySymbol="€"/>
                                    </div>
                                </div>
                            </div>

                            <div class="carrito-items" style="padding:0; border:none; box-shadow:none;">
                                <c:forEach items="${pedido.lineas}" var="linea">
                                    
                                    <c:set var="extension" value="" />
                                    <c:if test="${not fn:contains(linea.productoObj.imagen, '.')}">
                                        <c:set var="extension" value=".jpg" />
                                    </c:if>

                                    <div class="item-fila" style="border-bottom: 1px solid #eee;">
                                        <img src="${pageContext.request.contextPath}/img/productos/${linea.productoObj.imagen}${extension}" 
                                             class="item-img" 
                                             alt="${linea.productoObj.nombre}"
                                             onerror="this.src='${pageContext.request.contextPath}/img/default.jpg'">

                                        <div class="item-info">
                                            <h4>${linea.productoObj.nombre} <span style="font-size: 0.8em; color: #888;">(Ref: ${linea.productoObj.idProducto})</span></h4>
                                            <p>Precio Unitario: <fmt:formatNumber value="${linea.productoObj.precio}" maxFractionDigits="2"/> €</p>
                                        </div>

                                        <div class="item-controles" style="justify-content: flex-end;">
                                            <div style="text-align:right;">
                                                <p>Cantidad: <strong>${linea.cantidad}</strong></p>
                                                <p style="margin-top:5px; font-size:1.1em; color:#333;">
                                                    <strong><fmt:formatNumber value="${linea.productoObj.precio * linea.cantidad}" maxFractionDigits="2"/> €</strong>
                                                </p>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>

                    </c:forEach>
                </c:otherwise>
            </c:choose>

        </div>
        <jsp:include page="/jsp/fragmentos/_footer.jsp" />

    </body>
</html>