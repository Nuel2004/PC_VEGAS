<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:choose>
    <c:when test="${not empty productos}">
        <c:forEach var="p" items="${productos}">
            <c:set var="extension" value="" />
            <c:if test="${not fn:contains(p.imagen, '.')}">
                <c:set var="extension" value=".jpg" />
            </c:if>

            <div class="card">
                <img src="${pageContext.request.contextPath}/img/productos/${p.imagen}${extension}" 
                     alt="${p.nombre}" 
                     title="${p.nombre}"
                     onerror="this.src='${pageContext.request.contextPath}/img/default.jpg'">

                <div>
                    <h4 class="card-title">${p.nombre}</h4>
                    <div class="price">
                        <fmt:formatNumber value="${p.precio}" type="number" minFractionDigits="2" maxFractionDigits="2" var="precioFmt"/>
                        ${precioFmt} €
                    </div>
                </div>

                <button type="button" class="btn-add"
                        data-id="${p.idProducto}"
                        data-nombre="${fn:escapeXml(p.nombre)}"
                        data-desc="${fn:escapeXml(p.descripcion)}"
                        data-precio="${precioFmt} €"
                        data-img="${pageContext.request.contextPath}/img/productos/${p.imagen}${extension}"
                        onclick="abrirModal(this)">
                    Ver Detalle
                </button>
            </div>
        </c:forEach>
    </c:when>
    <c:otherwise>
        <div style="grid-column: span 100%; text-align: center; padding: 60px; background:#fff; border-radius:10px; border: 1px solid #eee;">
            <h3 style="color:#888;">No se encontraron productos con esos filtros.</h3>
        </div>
    </c:otherwise>
</c:choose>