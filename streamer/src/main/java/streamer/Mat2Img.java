package streamer;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

/**
 * Pas utilisé
 */

public class Mat2Img {

    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private Mat mat = new Mat();
    private BufferedImage img;

    public Mat2Img() {
    }

    public Mat2Img(Mat mat) {
        getSpace(mat);
    }

    public void getSpace(Mat mat) {
        int type = 0;
        if (mat.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (mat.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        this.mat = mat;
        int w = mat.cols();
        int h = mat.rows();
        if (img == null || img.getWidth() != w || img.getHeight() != h || img.getType() != type)
            img = new BufferedImage(w, h, type);
    }

    public BufferedImage getImage(Mat mat){
        getSpace(mat);
        WritableRaster raster = img.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        mat.get(0, 0, data);
        return img;
    }
}
