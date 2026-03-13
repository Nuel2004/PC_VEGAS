<%@page contentType="text/html" pageEncoding="UTF-8"%>

<style>
    .site-footer {
        background-color: #2c3e50; /* Azul oscuro elegante */
        color: #ecf0f1;
        padding: 40px 0 20px;
        margin-top: 60px; /* Separación del contenido principal */
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    }

    .footer-container {
        max-width: 1200px;
        margin: 0 auto;
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
        padding: 0 20px;
    }

    .footer-col {
        flex: 1;
        min-width: 250px;
        margin-bottom: 20px;
        padding-right: 20px;
    }

    .footer-col h3 {
        color: #3498db; 
        font-size: 1.2em;
        margin-bottom: 20px;
        border-bottom: 2px solid #3498db;
        display: inline-block;
        padding-bottom: 5px;
    }

    .footer-col p {
        font-size: 0.95em;
        line-height: 1.6;
        color: #bdc3c7;
    }

    .footer-links {
        list-style: none;
        padding: 0;
    }

    .footer-links li {
        margin-bottom: 10px;
    }

    .footer-links a {
        color: #ecf0f1;
        text-decoration: none;
        transition: color 0.3s;
    }

    .footer-links a:hover {
        color: #3498db;
        padding-left: 5px;
    }

    .footer-bottom {
        text-align: center;
        margin-top: 40px;
        padding-top: 20px;
        border-top: 1px solid #3e5871;
        font-size: 0.9em;
        color: #95a5a6;
    }
</style>

<footer class="site-footer">
    <div class="footer-container">

        <div class="footer-col">
            <h3>Sobre PC VEGAS</h3>
            <p>
                Somos tu tienda de confianza especializada en componentes informáticos, 
                periféricos y ordenadores a medida. Calidad y servicio técnico garantizado.
            </p>
        </div>

        <div class="footer-col">
            <h3>Contacto</h3>
            <ul class="footer-links">
                <li>📍 C/ De Arcos, 62 Montijo.</li>
                <li>📞 649 25 21 25</li>
                <li>📧 info@pcvegas.es</li>
                <li>⏰ L-V: 10:00 - 20:00</li>
            </ul>
        </div>

    </div>

    <div class="footer-bottom">
        &copy; 2026 PC VEGAS S.L. - Todos los derechos reservados.
    </div>
</footer>