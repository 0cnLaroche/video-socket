package streamer;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class CaptureVideo extends Thread {

	private static final int FPS = 30; // Frame Per Second
	private ByteBuffer buffer;
	private DataOutputStream out;
	private DataInputStream in;
	private boolean paused;
	private VideoCapture vc;
	private int sequence = 0;
	
    private ImageConverter converter;

	static{
		nu.pattern.OpenCV.loadShared();
	}
	
	public CaptureVideo(DataOutputStream out, DataInputStream in) throws Exception {
		vc = new VideoCapture();
		this.out = out;
		this.in = in;
		this.converter = new ImageConverter();
	}
	
	public void mirror() {

	}
	
	public void run() {

		capture(out, in);
	}
	
	public ByteBuffer getBuffer() {
		return this.buffer;
	}
	public void pause() {
		System.out.println("Capture vidéo mise en pause");
		this.paused = true;
	}
	
	public void resumeCapture() {
		System.out.println("Capture vidéo reprise");
		this.paused = false;
	}

	/**
	 * Utilise la camera par défaut pour prendre 1 image à toutes les 100 ms (soit 10 images par seconde = 10 fps).
	 * Une image représente une matrice de pixels. Chaque pixel = 1 octet. Un numéro de séquence est ajouté à chaque
	 * image afin de pouvoir être replacé dans le bonne ordre par le Viewer.
	 * @param out
	 * @throws IOException
	 * @throws InterruptedException
	 */
	
	private synchronized void capture(DataOutputStream out, DataInputStream in) {
		
		// Créer une instance de la webcam par defaut

		// Ouvrir la camera par defaut
		vc.open(0);

		System.out.println("Débute la capture vidéo à partir de la caméra par défaut");
        

        	try {

				while(vc.isOpened()) {

					Mat mat = new Mat();

					// Prend une capture d'écran
					vc.read(mat);

					// Créer un vecteur d'octets correspondant à la grandeur de l'image. 1 octet par pixel * channels (couleurs + gris)
					byte[] raw = new byte[(int) mat.total() * mat.channels()];

					// Aller chercher les octets et les placer dans le vecteur
					mat.get(0, 0, raw);

					// Créer un objet Serializable qui sera envoyé au serveur
					SerializableImage img = new SerializableImage(raw, mat.width(), mat.height(), mat.channels(), this.sequence);
					//this.sequence++;
					
					BufferedImage bimg = converter.getImage(img);
		            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		            tx.translate(-bimg.getWidth(null), 0);
		            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		            bimg = op.filter(bimg, null);
					
		            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		            ImageIO.write(bimg, "JPEG", buffer);
		            out.writeInt(buffer.size());
		            ImageIO.write(bimg, "JPEG", out);
		            out.write('y');
		            out.flush();
		            buffer.close();
		            	

		            while (in.read() != 'y')

					Thread.sleep(1000 / FPS);

				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

        System.out.println("Capture Vidéo terminé \nFrames envoyés: " + this.sequence);

	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
}