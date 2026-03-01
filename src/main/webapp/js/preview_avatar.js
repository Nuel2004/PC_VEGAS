document.addEventListener("DOMContentLoaded", function () {

    // CONFIGURACIÓN DE ELEMENTOS
    // Buscamos los inputs tanto de registro (ficheroAvatar) como de perfil (newAvatar)
    const inputAvatar = document.getElementById("ficheroAvatar") || document.getElementById("newAvatar");
    const imgPreview = document.getElementById("imgPreview");

    // Elementos de texto/botones
    const spanNombre = document.getElementById("nombreArchivo"); // En registro
    const smallAviso = document.getElementById("avisoFoto");     // En perfil
    const btnSeleccionar = document.getElementById("btnSelectImage"); // Botón en registro

    // 1. EVENTO: CLIC EN BOTÓN PERSONALIZADO (Solo Registro)
    if (btnSeleccionar && inputAvatar) {
        btnSeleccionar.addEventListener("click", function () {
            inputAvatar.click();
        });
    }

    // 2. EVENTO: CAMBIO EN EL INPUT (Cuando seleccionan foto)
    if (inputAvatar) {
        inputAvatar.addEventListener("change", function () {
            const file = this.files[0];

            if (file) {
                // A) Validar tipo de archivo
                if (!file.type.startsWith("image/")) {
                    alert("Por favor, selecciona un archivo de imagen válido.");
                    this.value = ""; // Limpiar input
                    return;
                }

                // B) Actualizar Textos
                // Caso Registro: Muestra el nombre del archivo
                if (spanNombre) {
                    spanNombre.textContent = file.name;
                    spanNombre.style.color = "#333";
                }
                // Caso Perfil: Muestra mensaje de éxito
                if (smallAviso) {
                    smallAviso.style.display = "block";
                }

                // C) Previsualizar Imagen (FileReader)
                const reader = new FileReader();
                reader.onload = function (e) {
                    if (imgPreview) {
                        imgPreview.src = e.target.result;
                    }
                }
                reader.readAsDataURL(file); // Leer imagen

            } else {
                // Si el usuario cancela la selección
                if (spanNombre)
                    spanNombre.textContent = "Ningún archivo seleccionado";
                if (smallAviso)
                    smallAviso.style.display = "none";
            }
        });
    }
});