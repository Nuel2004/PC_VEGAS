/* Archivo: web/js/carrito_ajax.js */

function anadirAlCarrito(idProducto) {
    console.log("--> Intentando añadir producto ID:", idProducto);

    const contador = document.getElementById('contador-carrito');
    if (contador) {
        contador.style.color = "#e74c3c"; 
    }

    const params = new URLSearchParams();
    params.append('operacion', 'add');
    params.append('idProducto', idProducto);

    fetch('CarritoAjaxController', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: params
    })
    .then(response => response.json())
    .then(data => {
        console.log("Respuesta del servidor:", data);
        
        if (data.ok) {
            if (contador) {
                contador.innerText = "(" + data.totalArticulos + ")";
                
                contador.style.transition = "all 0.3s";
                contador.style.transform = "scale(1.4)";
                contador.style.color = "#2ecc71"; 
                
                setTimeout(() => {
                    contador.style.transform = "scale(1)";
                    contador.style.color = ""; 
                }, 400);
            }
        } else {
            alert("Error: " + data.msg);
        }
    })
    .catch(error => {
        console.error('Error AJAX:', error);
        alert("Hubo un error de conexión al intentar añadir el producto.");
    });
}