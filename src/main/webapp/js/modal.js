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
// Función para añadir al carrito de forma asíncrona (AJAX)
function agregarAlCarritoAjax(idProducto) {
    // 1. Preparamos los parámetros (operación=add y el id)
    let params = new URLSearchParams();
    params.append("operacion", "add"); // Ahora usamos operacion=add en lugar de accion=add
    params.append("idProducto", idProducto);

    // 2. Enviamos la petición directamente a tu CarritoAjaxController
    fetch('CarritoAjaxController', {
        method: 'POST',
        body: params
    })
            .then(response => response.json())
            .then(data => {
                if (data.ok) {
                    // 3. Actualizamos el span del contador en el menú superior
                    const spanContador = document.getElementById("contador-carrito");
                    if (spanContador) {
                        // Actualiza el número (usando data.totalArticulos que devuelve tu JSON)
                        spanContador.innerText = "(" + data.totalArticulos + ")";

                        // Animación visual (el número hace un zoom y se pone verde)
                        spanContador.style.display = "inline-block";
                        spanContador.style.transition = "all 0.3s";
                        spanContador.style.color = "#2ecc71";
                        spanContador.style.transform = "scale(1.3)";

                        setTimeout(() => {
                            spanContador.style.color = "";
                            spanContador.style.transform = "scale(1)";
                        }, 400);
                    }

                    // Cerramos el modal
                    if (typeof cerrarModal === 'function') {
                        cerrarModal();
                    }
                }
            })
            .catch(error => console.error('Error al añadir al carrito:', error));
}

