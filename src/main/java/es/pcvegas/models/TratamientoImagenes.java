package es.pcvegas.model;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.servlet.http.Part;

/**
 * Clase de utilidad para la manipulación y procesamiento de imágenes. Se
 * encarga principalmente de redimensionar los avatares de usuario para mantener
 * un tamaño uniforme en la aplicación.
 *
 * * @author manuel
 */
public class TratamientoImagenes {

    /**
     * Procesa un archivo subido (Part), lo redimensiona a 100x100 píxeles y lo
     * guarda en disco. Utiliza las librerías gráficas de AWT (BufferedImage,
     * Graphics2D).
     *
     * * @param part El objeto Part que contiene el archivo subido.
     * @param rutaDirectorio Ruta del sistema de archivos donde se guardará la
     * imagen.
     * @param nombreFinal Nombre del archivo destino (incluyendo extensión
     * .jpg).
     * @return true si la operación fue exitosa, false si hubo error.
     */
    public static boolean guardarAvatar(Part part, String rutaDirectorio, String nombreFinal) {
        try (InputStream input = part.getInputStream()) {
            BufferedImage imagenOriginal = ImageIO.read(input);
            if (imagenOriginal == null) {
                return false; // No es una imagen válida
            }

            Image escalada = imagenOriginal.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            BufferedImage bufferedEscalada = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bufferedEscalada.createGraphics();
            g2d.drawImage(escalada, 0, 0, null);
            g2d.dispose();

            File directorio = new File(rutaDirectorio);

            File archivoFinal = new File(directorio, nombreFinal);
            ImageIO.write(bufferedEscalada, "jpg", archivoFinal);

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
