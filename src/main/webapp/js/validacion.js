/* Archivo: js/validacion.js */

document.addEventListener("DOMContentLoaded", function () {

    // ========================================================================
    // 1. VALIDACIÓN DEL FORMULARIO DE REGISTRO
    // ========================================================================
    const formRegistro = document.querySelector("form[action*='RegistroController']");

    if (formRegistro) {

        // A) Comprobación AJAX de Email (Se mantiene igual)
        const emailInput = formRegistro.querySelector("#email");
        if (emailInput) {
            emailInput.addEventListener("blur", function () {
                let email = this.value;
                if (email.length > 0) {
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

        // B) Validación al enviar (Submit)
        formRegistro.addEventListener("submit", function (e) {
            let valido = true;
            limpiarErrores(this);

            // --- 1. LIMPIEZA ANTI-ESPACIOS (AQUÍ ESTÁ LA CLAVE) ---
            // Añadimos 'nif_numeros' y 'telefono' para quitar espacios antes de validar
            const camposTexto = ["nombre", "apellidos", "direccion", "nif_numeros", "telefono"];

            camposTexto.forEach(id => {
                const input = this.querySelector("#" + id);
                if (input) {
                    // Limpiamos espacios visualmente
                    input.value = input.value.trim();

                    // Verificamos si quedó vacío (excepto password que va aparte)
                    if (input.value.length === 0) {
                        mostrarError(input, "Este campo es obligatorio.");
                        valido = false;
                    }
                }
            });

            // Chequeo de password aparte (para no hacer trim si no quieres)
            const pass1 = this.querySelector("#password");
            if (pass1 && pass1.value.trim().length === 0) {
                mostrarError(pass1, "La contraseña es obligatoria.");
                valido = false;
            }

            // --- 2. VALIDACIONES ESPECÍFICAS ---

            if (emailInput && emailInput.hasAttribute("data-invalid")) {
                mostrarError(emailInput, "El email ya existe. Usa otro.");
                valido = false;
            }

            const pass2 = this.querySelector("#password_repetida");
            if (pass1 && pass2) {
                if (pass1.value !== pass2.value) {
                    mostrarError(pass2, "Las contraseñas no coinciden.");
                    valido = false;
                } else if (pass1.value.length < 4) {
                    mostrarError(pass1, "La contraseña es muy corta (mín. 4).");
                    valido = false;
                }
            }

            // Validación DNI: Ahora que ya hicimos .trim() arriba, el regex funcionará bien
            const nifInput = this.querySelector("#nif_numeros");
            if (nifInput && !/^\d{8}$/.test(nifInput.value)) {
                mostrarError(nifInput, "El DNI debe tener 8 números exactos.");
                valido = false;
            }

            const telInput = this.querySelector("#telefono");
            if (telInput && telInput.value.length > 0 && !/^[679]\d{8}$/.test(telInput.value)) {
                mostrarError(telInput, "Teléfono inválido (9 dígitos).");
                valido = false;
            }

            const cpInput = this.querySelector("#codigo_postal");
            if (cpInput && !/^(?:0[1-9]|[1-4]\d|5[0-2])\d{3}$/.test(cpInput.value)) {
                mostrarError(cpInput, "CP incorrecto (01xxx - 52xxx).");
                valido = false;
            }

            if (!valido)
                e.preventDefault();
        });
    }

    // ========================================================================
    // 2. VALIDACIÓN DEL FORMULARIO DE PERFIL (Igual que antes)
    // ========================================================================
    const formPerfil = document.querySelector("form[action*='PerfilController']");
    if (formPerfil) {
        formPerfil.addEventListener("submit", function (e) {
            let valido = true;
            limpiarErrores(this);

            const camposTexto = ["nombre", "apellidos", "direccion"];
            camposTexto.forEach(id => {
                const input = this.querySelector("#" + id);
                if (input) {
                    input.value = input.value.trim();
                    if (input.value.length === 0) {
                        mostrarError(input, "Este campo es obligatorio.");
                        valido = false;
                    }
                }
            });

            const nifInput = this.querySelector("#nif");
            if (nifInput && !nifInput.readOnly) {
                const nif = nifInput.value.toUpperCase().trim();
                if (!/^[0-9]{8}[TRWAGMYFPDXBNJZSQVHLCKE]$/.test(nif)) {
                    mostrarError(nifInput, "Formato incorrecto (8 números + Letra).");
                    valido = false;
                } else {
                    const letras = "TRWAGMYFPDXBNJZSQVHLCKE";
                    const numeros = nif.substring(0, 8);
                    const letraCorrecta = letras.charAt(parseInt(numeros) % 23);
                    if (nif.charAt(8) !== letraCorrecta) {
                        mostrarError(nifInput, "Letra incorrecta. Debería ser: " + letraCorrecta);
                        valido = false;
                    }
                }
            }

            const telInput = this.querySelector("#telefono");
            if (telInput) {
                telInput.value = telInput.value.trim();
                if (telInput.value.length === 0) {
                    mostrarError(telInput, "El teléfono es obligatorio.");
                    valido = false;
                } else if (!/^[679]\d{8}$/.test(telInput.value)) {
                    mostrarError(telInput, "Teléfono inválido.");
                    valido = false;
                }
            }

            const cpInput = this.querySelector("#codigoPostal");
            if (cpInput && cpInput.value.trim().length > 0 && !/^(?:0[1-9]|[1-4]\d|5[0-2])\d{3}$/.test(cpInput.value.trim())) {
                mostrarError(cpInput, "CP incorrecto.");
                valido = false;
            }

            if (!valido)
                e.preventDefault();
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
            if (i.id !== 'email' || i.style.borderColor !== 'rgb(46, 204, 113)') {
                i.style.borderColor = "#ccc";
            }
        });
        const errores = form.querySelectorAll(".msg-error-js");
        errores.forEach(e => e.remove());
    }

    function limpiarMensajeCampo(input) {
        const hermano = input.nextElementSibling;
        if (hermano && hermano.className === 'msg-error-js') {
            hermano.remove();
        }
    }
});