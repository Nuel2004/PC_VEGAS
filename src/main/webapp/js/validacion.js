document.addEventListener("DOMContentLoaded", function () {

    // ========================================================================
    // 1. VALIDACIÓN DEL FORMULARIO DE REGISTRO
    // ========================================================================
    const formRegistro = document.querySelector("form[action*='RegistroController']");
    
    if (formRegistro) {
        
        // A) Lógica AJAX para comprobar Email (Igual que antes)
        const emailInput = formRegistro.querySelector("#email");
        if (emailInput) {
            emailInput.addEventListener("blur", function() {
                let email = this.value;
                if(email.length > 0) {
                    limpiarMensajeCampo(this);
                    fetch('RegistroController', {
                        method: 'POST',
                        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                        body: 'accion=comprobarEmail&email=' + encodeURIComponent(email)
                    })
                    .then(response => response.text())
                    .then(texto => {
                        if (texto.trim() === "OCUPADO") {
                            mostrarError(this, "⚠️ Este correo ya está registrado.");
                            this.style.borderColor = "#e74c3c";
                            this.setAttribute("data-invalid", "true");
                        } else {
                            this.style.borderColor = "#2ecc71";
                            this.removeAttribute("data-invalid");
                        }
                    })
                    .catch(err => console.error("Error AJAX:", err));
                }
            });
        }

        // B) Validación al enviar (Submit) con reglas estrictas
        formRegistro.addEventListener("submit", function (e) {
            let valido = true;
            limpiarErrores(this);

            if (emailInput && emailInput.hasAttribute("data-invalid")) {
                mostrarError(emailInput, "El email ya existe. Usa otro.");
                valido = false;
            }

            const pass1 = this.querySelector("#password");
            const pass2 = this.querySelector("#password_repetida");
            if (pass1.value !== pass2.value) {
                mostrarError(pass2, "Las contraseñas no coinciden.");
                valido = false;
            } else if (pass1.value.length < 4) {
                mostrarError(pass1, "La contraseña es muy corta.");
                valido = false;
            }

            const nifInput = this.querySelector("#nif_numeros");
            if (nifInput && !/^\d{8}$/.test(nifInput.value)) {
                mostrarError(nifInput, "El DNI debe tener exactamente 8 números.");
                valido = false;
            }

            // --- VALIDACIÓN ESTRICTA DE TELÉFONO ESPAÑOL ---
            const telInput = this.querySelector("#telefono");
            // Empieza por 6, 7 o 9 y tiene 8 dígitos más (Total 9)
            if (telInput.value.length > 0 && !/^[679]\d{8}$/.test(telInput.value)) {
                mostrarError(telInput, "Teléfono no válido (9 dígitos, debe empezar por 6, 7 o 9).");
                valido = false;
            }

            // --- VALIDACIÓN ESTRICTA DE CP ESPAÑOL ---
            const cpInput = this.querySelector("#codigo_postal");
            // Rango 01-52 seguido de 3 dígitos cualquiera
            if (cpInput && !/^(?:0[1-9]|[1-4]\d|5[0-2])\d{3}$/.test(cpInput.value)) {
                mostrarError(cpInput, "CP incorrecto. Debe ser un código español válido (01xxx - 52xxx).");
                valido = false;
            }

            if (!valido) {
                e.preventDefault();
            }
        });
    }

    // ========================================================================
    // 2. VALIDACIÓN DEL FORMULARIO DE PERFIL
    // ========================================================================
    const formPerfil = document.querySelector("form[action*='PerfilController']");
    
    if (formPerfil) {
        formPerfil.addEventListener("submit", function (e) {
            let valido = true;
            limpiarErrores(this);

            const nifInput = this.querySelector("#nif");
            if (nifInput && !nifInput.readOnly) {
                const nif = nifInput.value.toUpperCase();
                // 8 números + letra válida
                if (!/^[0-9]{8}[TRWAGMYFPDXBNJZSQVHLCKE]$/.test(nif)) {
                    mostrarError(nifInput, "Formato incorrecto (8 números + Letra).");
                    valido = false;
                } else {
                    const letras = "TRWAGMYFPDXBNJZSQVHLCKE";
                    const numeros = nif.substring(0, 8);
                    const letraCorrecta = letras.charAt(parseInt(numeros) % 23);
                    if (nif.charAt(8) !== letraCorrecta) {
                        mostrarError(nifInput, "La letra es incorrecta. Debería ser: " + letraCorrecta);
                        valido = false;
                    }
                }
            }

            // --- VALIDACIÓN ESTRICTA DE TELÉFONO ESPAÑOL ---
            const telInput = this.querySelector("#telefono");
            if (telInput.value.length > 0 && !/^[679]\d{8}$/.test(telInput.value)) {
                mostrarError(telInput, "Teléfono no válido (9 dígitos, debe empezar por 6, 7 o 9).");
                valido = false;
            }

            // --- VALIDACIÓN ESTRICTA DE CP ESPAÑOL ---
            // Nota: En perfil el ID suele ser camelCase (#codigoPostal)
            const cpInput = this.querySelector("#codigoPostal"); 
            if (cpInput && !/^(?:0[1-9]|[1-4]\d|5[0-2])\d{3}$/.test(cpInput.value)) {
                mostrarError(cpInput, "CP incorrecto. Debe ser un código español válido (01xxx - 52xxx).");
                valido = false;
            }

            if (!valido) {
                e.preventDefault();
            }
        });
    }

    // ========================================================================
    // 3. FUNCIONES AUXILIARES
    // ========================================================================
    
    function mostrarError(input, mensaje) {
        input.style.borderColor = "#e74c3c";
        const divError = document.createElement("div");
        divError.className = "msg-error-js";
        divError.innerText = mensaje;
        divError.style.color = "#e74c3c";
        divError.style.fontSize = "0.85em";
        divError.style.marginTop = "5px";
        divError.style.fontWeight = "bold";
        
        if (input.parentNode.classList.contains('form-group') && input.parentNode.children.length > 2) {
             input.parentNode.appendChild(divError);
        } else {
             input.parentNode.insertBefore(divError, input.nextSibling);
        }
    }

    function limpiarErrores(form) {
        const inputs = form.querySelectorAll("input");
        inputs.forEach(i => {
            if(i.id !== 'email' || i.style.borderColor !== 'rgb(46, 204, 113)') {
                i.style.borderColor = "#ccc";
            }
        });
        const errores = form.querySelectorAll(".msg-error-js");
        errores.forEach(e => e.remove());
    }

    function limpiarMensajeCampo(input) {
        const hermano = input.nextElementSibling;
        if(hermano && hermano.className === 'msg-error-js') {
            hermano.remove();
        }
    }
});