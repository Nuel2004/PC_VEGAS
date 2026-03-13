/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


// Archivo: Web Pages/js/modal.js

function abrirModal(boton) {
    const modal = document.getElementById("productoModal");

    const id = boton.getAttribute("data-id");
    const nombre = boton.getAttribute("data-nombre");
    const desc = boton.getAttribute("data-desc");
    const precio = boton.getAttribute("data-precio");
    const img = boton.getAttribute("data-img");

    document.getElementById("modalIdProducto").value = id;
    document.getElementById("modalNombre").innerText = nombre;

    document.getElementById("modalDesc").innerText = desc ? desc : "No hay descripción disponible para este producto.";

    document.getElementById("modalPrecio").innerText = precio;
    document.getElementById("modalImg").src = img;

    modal.style.display = "flex";
}

function cerrarModal() {
    const modal = document.getElementById("productoModal");
    modal.style.display = "none";
}

window.onclick = function (event) {
    const modal = document.getElementById("productoModal");
    if (event.target === modal) {
        cerrarModal();
    }
};
function agregarAlCarritoAjax(idProducto) {
    
    if (!idProducto) {
        idProducto = document.getElementById("modalIdProducto").value;
    }

    if (typeof anadirAlCarrito === "function") {
        anadirAlCarrito(idProducto);
        cerrarModal(); 
    } else {
        console.error("Error: No se encuentra la función añadirAlCarrito. Verifica que _header.jsp carga carrito_ajax.js");
    }
}