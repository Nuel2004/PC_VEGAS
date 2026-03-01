package es.pcvegas.model;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.servlet.http.Part;

public class TratamientoImagenes {

    /**
     * Redimensiona la imagen y la guarda en la carpeta especificada. Devuelve
     * true si todo ha ido bien, o false si hubo algún fallo.
     */
    public static boolean guardarAvatar(Part part, String rutaDirectorio, String nombreFinal) {
        try (InputStream input = part.getInputStream()) {
            BufferedImage imagenOriginal = ImageIO.read(input);
            if (imagenOriginal == null) {
                return false; // No es una imagen válida
            }

            // 1. Redimensionar a 100x100
            Image escalada = imagenOriginal.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            BufferedImage bufferedEscalada = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bufferedEscalada.createGraphics();
            g2d.drawImage(escalada, 0, 0, null);
            g2d.dispose();

            // 3. Guardar el archivo final (.jpg)
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
