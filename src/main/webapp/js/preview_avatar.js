document.addEventListener("DOMContentLoaded", function () {

    const inputAvatar = document.getElementById("ficheroAvatar") || document.getElementById("newAvatar");
    const imgPreview = document.getElementById("imgPreview");

    const spanNombre = document.getElementById("nombreArchivo"); 
    const smallAviso = document.getElementById("avisoFoto");     
    const btnSeleccionar = document.getElementById("btnSelectImage"); 

    if (btnSeleccionar && inputAvatar) {
        btnSeleccionar.addEventListener("click", function () {
            inputAvatar.click();
        });
    }

    if (inputAvatar) {
        inputAvatar.addEventListener("change", function () {
            const file = this.files[0];

            if (file) {
                if (!file.type.startsWith("image/")) {
                    alert("Por favor, selecciona un archivo de imagen válido.");
                    this.value = ""; 
                    return;
                }

                if (spanNombre) {
                    spanNombre.textContent = file.name;
                    spanNombre.style.color = "#333";
                }
                if (smallAviso) {
                    smallAviso.style.display = "block";
                }

                const reader = new FileReader();
                reader.onload = function (e) {
                    if (imgPreview) {
                        imgPreview.src = e.target.result;
                    }
                }
                reader.readAsDataURL(file); 

            } else {
                if (spanNombre)
                    spanNombre.textContent = "Ningún archivo seleccionado";
                if (smallAviso)
                    smallAviso.style.display = "none";
            }
        });
    }
});