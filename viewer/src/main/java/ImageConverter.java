
import org.opencv.core.Core;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

/**
 * Convertis un objet SerializableImage (généré par le Streamer et reçu pas le Viewer) en BufferedImage qui pourra
 * être affiché à l'écran par Java Swing.
 */

public class ImageConverter {

    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private SerializableImage simg;
    private BufferedImage bimg;

    public ImageConverter() {
    }

    public ImageConverter(SerializableImage img) {
        getSpace(img);
    }

    public void getSpace(SerializableImage img) {
        int type = 0;
        if (img.getChannels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (img.getChannels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        this.simg = img;
        int w = img.getWidth();
        int h = img.getHeight();
        if (bimg == null || bimg.getWidth() != w || bimg.getHeight() != h || bimg.getType() != type)
            bimg = new BufferedImage(w, h, type);
    }

    public BufferedImage getImage(SerializableImage img){
        getSpace(img);
        WritableRaster raster = bimg.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();

        byte[] data = dataBuffer.getData();

        for (int i = 0; i < data.length; i++) {
            data[i] = img.getRaw()[i];
        }
        return bimg;
    }
}
