<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>PC VEGAS - Tu Tienda de Informática</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tienda.css">    
        <style>
            .filtros-box {
                background: #f9f9f9;
                padding: 15px;
                border-radius: 8px;
                margin-bottom: 20px;
                border: 1px solid #ddd;
            }
            .filter-group {
                margin-bottom: 15px;
            }
            .filter-group input[type="text"], .filter-group select {
                width: 100%;
                padding: 8px;
                border: 1px solid #ccc;
                border-radius: 4px;
                box-sizing: border-box;
            }
            .filter-group input[type="range"] {
                width: 100%;
                margin-top: 5px;
                cursor: pointer;
            }
            .btn-filtrar {
                width: 100%;
                padding: 10px;
                background-color: #3498db;
                color: white;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                font-weight: bold;
            }
            .btn-filtrar:hover {
                background-color: #2980b9;
            }
            .filter-group select[multiple] {
                min-height: 120px;
                padding: 8px;
            }
            /* Estilo para la lista de categorías */
            .cat-list {
                list-style: none;
                padding: 0;
                margin: 0;
            }
            .cat-list li {
                margin-bottom: 8px;
            }
            .cat-btn {
                background: none;
                border: none;
                color: #333;
                cursor: pointer;
                font-size: 1em;
                text-align: left;
                padding: 5px;
                width: 100%;
                border-bottom: 1px solid #eee;
                transition: all 0.2s;
            }
            .cat-btn:hover {
                color: #3498db;
                padding-left: 10px;
                border-bottom: 1px solid #3498db;
            }
        </style>
    </head>
    <body>

        <jsp:include page="/jsp/fragmentos/_header.jsp" />

        <main class="main-wrapper">

            <aside class="sidebar-left">

                <button type="button" id="btnIrTienda" class="btn-ir-tienda" style="margin-top: 10px; width: 100%;">
                    TODOS LOS PRODUCTOS
                </button>

                <div class="filtros-box">
                    <h3 style="margin-top:0;">Buscar Productos</h3>
                    <form action="${pageContext.request.contextPath}/inicio" method="post" id="formFiltros">

                        <input type="hidden" name="idCategoria" id="inputCategoria" value="">

                        <div class="filter-group">
                            <input type="text" name="busqueda" placeholder="Buscar por nombre..." value="${param.busqueda}">
                        </div>

                        <div class="filter-group">
                            <select name="marca" multiple size="6" title="Selecciona una o varias marcas">
                                <c:forEach items="${applicationScope.listaMarcas}" var="m">
                                    <option value="${m}" <c:if test="${not empty paramValues.marca && fn:contains(fn:join(paramValues.marca, ','), m)}">selected</c:if>>${m}</option>
                                </c:forEach>
                            </select>
                            <small style="display:block; margin-top:6px; color:#666;">(Si no seleccionas ninguna marca, se mostrarán todas)</small>
                        </div>

                        <div class="filter-group">
                            <label style="font-size: 0.9em; color: #555;">
                                Precio Mín: <strong><span id="precioMinVal"><fmt:formatNumber value="${not empty param.precioMin ? param.precioMin : applicationScope.precioMinimo}" maxFractionDigits="0"/></span> €</strong>
                            </label>
                            <input type="range" name="precioMin" id="precioMin" 
                                   min="${applicationScope.precioMinimo}" max="${applicationScope.precioMaximo}" 
                                   value="${not empty param.precioMin ? param.precioMin : applicationScope.precioMinimo}">
                        </div>

                        <div class="filter-group">
                            <label style="font-size: 0.9em; color: #555;">
                                Precio Máx: <strong><span id="precioMaxVal"><fmt:formatNumber value="${not empty param.precioMax ? param.precioMax : applicationScope.precioMaximo}" maxFractionDigits="0"/></span> €</strong>
                            </label>
                            <input type="range" name="precioMax" id="precioMax" 
                                   min="${applicationScope.precioMinimo}" max="${applicationScope.precioMaximo}" 
                                   value="${not empty param.precioMax ? param.precioMax : applicationScope.precioMaximo}">
                        </div>

                        <button type="submit" class="btn-filtrar">🔍 Aplicar Filtros</button>
                    </form>
                </div>

                <div class="filtros-box">
                    <h3 style="margin-top:0;">Categorías</h3>
                    <ul class="cat-list">
                        <c:forEach items="${applicationScope.listaCategorias}" var="c">
                            <li>
                                <button type="button" class="cat-btn" onclick="filtrarPorCategoria(${c.idCategoria})"> > ${c.nombre} </button>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </aside>

            <section class="products-right">
                <h2>Escaparate</h2>
                <div class="grid" id="gridProductos">
                    <c:choose>
                        <c:when test="${not empty productos}">
                            <c:forEach var="p" items="${productos}">
                                <c:set var="extension" value="" />
                                <c:if test="${not fn:contains(p.imagen, '.')}"><c:set var="extension" value=".jpg" /></c:if>

                                    <div class="card">
                                        <img src="${pageContext.request.contextPath}/img/productos/${p.imagen}${extension}" 
                                         alt="${p.nombre}" title="${p.nombre}"
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
                </div>
            </section>
        </main>

        <div id="productoModal" class="modal-overlay">
            <div class="modal-content">
                <span class="close-btn" onclick="cerrarModal()">&times;</span>
                <div class="modal-body">
                    <div class="modal-img-container">
                        <img id="modalImg" src="" alt="Detalle del Producto" title="Detalle del Producto" onerror="this.src='${pageContext.request.contextPath}/img/default.jpg'">
                    </div>
                    <div class="modal-info">
                        <div>
                            <h2 id="modalNombre">Nombre del Producto</h2>
                            <div class="modal-price" id="modalPrecio">0,00 €</div>
                            <p id="modalDesc">Descripción...</p>
                        </div>
                        <form action="${pageContext.request.contextPath}/CarritoController" method="post" target="frameOculto" style="margin-top: 20px;">
                            <input type="hidden" name="accion" value="add"/>
                            <input type="hidden" name="idproducto" id="modalIdProducto" value=""/>
                            <input type="hidden" name="modo" value="iframe"/>
                            <button type="submit" class="btn-add" style="font-size: 1.1em; padding: 15px;" onclick="setTimeout(cerrarModal, 100);">
                                🛒 AÑADIR AL CARRITO
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
                            <jsp:include page="/jsp/fragmentos/_footer.jsp" />

        <iframe name="frameOculto" id="frameOculto" style="display:none;"></iframe>
        <script src="${pageContext.request.contextPath}/js/modal.js"></script>

        <script>
                                let sliderMin = document.getElementById('precioMin');
                                let sliderMax = document.getElementById('precioMax');
                                let valMin = document.getElementById('precioMinVal');
                                let valMax = document.getElementById('precioMaxVal');

                                sliderMin.addEventListener('input', function () {
                                    if (parseInt(sliderMin.value) > parseInt(sliderMax.value)) {
                                        sliderMin.value = sliderMax.value;
                                    }
                                    valMin.innerText = sliderMin.value;
                                });
                                sliderMax.addEventListener('input', function () {
                                    if (parseInt(sliderMax.value) < parseInt(sliderMin.value)) {
                                        sliderMax.value = sliderMin.value;
                                    }
                                    valMax.innerText = sliderMax.value;
                                });

                                function lanzarBusquedaAjax() {
                                    let form = document.getElementById('formFiltros');
                                    let formData = new FormData(form);
                                    let params = new URLSearchParams(formData);
                                    fetch('${pageContext.request.contextPath}/BuscadorAjaxController', {method: 'POST', body: params})
                                            .then(response => response.text())
                                            .then(html => {
                                                document.getElementById('gridProductos').innerHTML = html;
                                            })
                                            .catch(error => console.error('Error al filtrar:', error));
                                }

                                document.getElementById('formFiltros').addEventListener('submit', function (e) {
                                    e.preventDefault();
                                    lanzarBusquedaAjax();
                                });

                                function filtrarPorCategoria(idCat) {
                                    document.getElementById('inputCategoria').value = idCat;
                                    document.getElementsByName('busqueda')[0].value = "";
                                    lanzarBusquedaAjax();
                                }

                                document.getElementById('btnIrTienda').addEventListener('click', function () {
                                    let form = document.getElementById('formFiltros');
                                    form.reset();
                                    document.getElementById('inputCategoria').value = "";
                                    sliderMin.value = sliderMin.min;
                                    sliderMax.value = sliderMax.max;
                                    valMin.innerText = sliderMin.value;
                                    valMax.innerText = sliderMax.value;
                                    lanzarBusquedaAjax();
                                });
        </script>
    </body>
</html>