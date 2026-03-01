/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


// Archivo: Web Pages/js/modal.js

// Función para rellenar y abrir el modal
function abrirModal(boton) {
    const modal = document.getElementById("productoModal");

    // 1. Leemos los datos que hemos escondido en el botón HTML
    const id = boton.getAttribute("data-id");
    const nombre = boton.getAttribute("data-nombre");
    const desc = boton.getAttribute("data-desc");
    const precio = boton.getAttribute("data-precio");
    const img = boton.getAttribute("data-img");

    // 2. Rellenamos el HTML del modal con esos datos
    document.getElementById("modalIdProducto").value = id;
    document.getElementById("modalNombre").innerText = nombre;

    // Si la descripción está vacía, ponemos un texto por defecto
    document.getElementById("modalDesc").innerText = desc ? desc : "No hay descripción disponible para este producto.";

    document.getElementById("modalPrecio").innerText = precio;
    document.getElementById("modalImg").src = img;

    // 3. Mostramos el modal
    modal.style.display = "flex";
}

// Función para cerrar el modal
function cerrarModal() {
    const modal = document.getElementById("productoModal");
    modal.style.display = "none";
}

// Cerrar también si el usuario hace clic fuera de la caja blanca del modal
window.onclick = function (event) {
    const modal = document.getElementById("productoModal");
    if (event.target === modal) {
        cerrarModal();
    }
};