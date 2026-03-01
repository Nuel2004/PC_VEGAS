document.addEventListener("DOMContentLoaded", function () {
    const inputNumeros = document.getElementById("nif_numeros");
    const inputLetra = document.getElementById("nif_letra");

    if (inputNumeros && inputLetra) {
        inputNumeros.addEventListener("input", function () {
            let numero = this.value;

            // Si ha escrito 8 números, calculamos la letra
            if (numero.length === 8 && /^\d+$/.test(numero)) {
                const letras = "TRWAGMYFPDXBNJZSQVHLCKE";
                const indice = parseInt(numero) % 23;
                const letraCorrecta = letras.charAt(indice);
                
                // Rellenamos la cajita de la letra automáticamente
                inputLetra.value = letraCorrecta;
                
                // Efecto visual de "éxito"
                inputLetra.style.backgroundColor = "#d4edda";
                inputLetra.style.color = "#155724";
                inputLetra.style.borderColor = "#c3e6cb";
            } else {
                // Si borra números, limpiamos la letra
                inputLetra.value = "";
                inputLetra.style.backgroundColor = "#e9ecef";
                inputLetra.style.color = "#495057";
                inputLetra.style.borderColor = "#ced4da";
            }
        });
    }
});