<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>PC Vegas - Carrito</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/carrito.css">    
    </head>
    <body>

        <jsp:include page="/jsp/fragmentos/_header.jsp" />

        <div class="carrito-container">
            <div class="carrito-items">
                <h2 style="margin-top:0;">Tu Cesta</h2>
                <hr style="border:0; border-top:2px solid #111; margin-bottom: 20px;">

                <c:choose>
                    <c:when test="${empty sessionScope.carrito or sessionScope.carrito.lineas.size() == 0}">
                        <div style="text-align:center; padding: 40px;">
                            <h3 style="color:#888;">El carrito está vacío :(</h3>
                            <a href="${pageContext.request.contextPath}/inicio" style="color:#e74c3c; font-weight:bold; margin-top:20px; display:inline-block;">Volver a la tienda</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${sessionScope.carrito.lineas}" var="linea">
                            <c:set var="extension" value="" />
                            <c:if test="${not fn:contains(linea.productoObj.imagen, '.')}"><c:set var="extension" value=".jpg" /></c:if>

                                <div class="item-fila">
                                    <img src="${pageContext.request.contextPath}/img/productos/${linea.productoObj.imagen}${extension}" 
                                     class="item-img" alt="${linea.productoObj.nombre}" title="${linea.productoObj.nombre}" 
                                     onerror="this.src='${pageContext.request.contextPath}/img/default.jpg'">

                                <div class="item-info">
                                    <h4>${linea.productoObj.nombre} <span style="font-size: 0.8em; color: #888; font-weight: normal;">(ID: ${linea.productoObj.idProducto})</span></h4>
                                    <p>Precio Unitario: <fmt:formatNumber value="${linea.productoObj.precio}" maxFractionDigits="2"/> €</p>
                                    <p><strong>Subtotal: <span id="subtot_${linea.productoObj.idProducto}"><fmt:formatNumber value="${linea.productoObj.precio * linea.cantidad}" minFractionDigits="2" maxFractionDigits="2"/></span> €</strong></p>
                                </div>

                                <div class="item-controles">
                                    <div style="display:flex; gap:5px; align-items:center;">
                                        <button type="button" onclick="actualizarCarrito(${linea.productoObj.idProducto}, 'menos')" class="btn-qty">-</button>
                                        <input type="number" id="cant_${linea.productoObj.idProducto}" value="${linea.cantidad}" style="width: 40px; text-align: center; border: 1px solid #ccc; border-radius:4px; padding: 4px;" readonly>
                                        <button type="button" onclick="actualizarCarrito(${linea.productoObj.idProducto}, 'mas')" class="btn-qty">+</button>
                                    </div>
                                    <form action="${pageContext.request.contextPath}/CarritoController" method="post" style="margin-top: 10px; text-align: center;">
                                        <input type="hidden" name="accion" value="eliminar">
                                        <input type="hidden" name="idProducto" value="${linea.productoObj.idProducto}">
                                        <button type="submit" style="background:none; border:none; color:#e74c3c; cursor:pointer; font-size:1.4em;" title="Eliminar producto">🗑</button>
                                    </form>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="carrito-resumen">
                <div class="resumen-box">
                    <h2 style="margin-top: 0; margin-bottom: 20px; text-align:center;">RESUMEN</h2>
                    <c:set var="total" value="${sessionScope.carrito.importe + sessionScope.carrito.iva}" />
                    <div class="resumen-linea">
                        <span>Base Imponible</span>
                        <span><span id="totalFinal"><fmt:formatNumber value="${sessionScope.carrito.importe}" type="number" minFractionDigits="2" maxFractionDigits="2"/></span> €</span>
                    </div>
                    <div class="resumen-linea">
                        <span>IVA (21%)</span>
                        <span><span id="totalIva"><fmt:formatNumber value="${sessionScope.carrito.iva}" type="number" minFractionDigits="2" maxFractionDigits="2"/></span> €</span>
                    </div>
                    <div class="resumen-linea total-linea">
                        <span>Total</span>
                        <span><span id="totalConIva"><fmt:formatNumber value="${total}" type="number" minFractionDigits="2" maxFractionDigits="2"/></span> €</span>
                    </div>
                    <div class="acciones-carrito">
                        <c:choose>
                            <c:when test="${not empty sessionScope.usuario}">
                                <form action="${pageContext.request.contextPath}/CarritoController" method="post" style="text-align:center;">
                                    <input type="hidden" name="accion" value="tramitar">
                                    <button type="submit" class="btn-comprar" style="width: 100%;">Tramitar Pedido</button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/login" style="text-align:center;">
                                    <button type="button" class="btn-comprar" style="width: 100%;">Inicia Sesión para Comprar</button>
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="/jsp/fragmentos/_footer.jsp" />

        <script>
            function actualizarCarrito(idProducto, operacion) {
                let params = new URLSearchParams();
                params.append("idProducto", idProducto);
                params.append("operacion", operacion);
                fetch('${pageContext.request.contextPath}/CarritoAjaxController', {method: 'POST', body: params})
                        .then(response => response.json())
                        .then(data => {
                            if (data.eliminado) {
                                location.reload();
                            } else {
                                let formato = new Intl.NumberFormat('es-ES', {minimumFractionDigits: 2, maximumFractionDigits: 2});
                                document.getElementById('cant_' + idProducto).value = data.cantidad;
                                document.getElementById('subtot_' + idProducto).innerText = formato.format(data.lineaSubtotal);
                                document.getElementById('totalFinal').innerText = formato.format(data.total);
                                document.getElementById('totalIva').innerText = formato.format(data.iva);
                                let sumaFinal = data.total + data.iva;
                                document.getElementById('totalConIva').innerText = formato.format(sumaFinal);
                            }
                        }).catch(error => console.error('Error en la petición AJAX:', error));
            }
        </script>
    </body>
</html>