import java.io.*;
import java.nio.ByteBuffer;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class CaptureVideo extends Thread {

	private static final int FPS = 10; // Frame Per Second
	private ByteBuffer buffer;
	private DataOutputStream out;
	private boolean paused;
	private VideoCapture vc;
	private int sequence = 0;

	static{
		nu.pattern.OpenCV.loadShared();
	}
	
	public CaptureVideo(DataOutputStream out) throws Exception {
		vc = new VideoCapture();
		this.out = out;
	}
	
	public void mirror() {

	}
	
	public void run() {
		
		try {
			capture(out);
		} catch (IOException e) {

			e.printStackTrace();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
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
	
	private synchronized void capture(DataOutputStream out) throws IOException, InterruptedException {
		
		// Créer une instance de la webcam par defaut

        ObjectOutputStream oos = new ObjectOutputStream(out);

		// Ouvrir la camera par defaut
		vc.open(0);

		System.out.println("Débute la capture vidéo à partir de la caméra par défaut");
        
        while(vc.isOpened()) {

        	Mat mat = new Mat();
        	// Prend une capture d'écran
			vc.read(mat);
			// Créer un vecteur d'octets correspondant à la grandeur de l'image. 1 octet par pixel * channels (couleurs + gris)
        	byte[] raw = new byte[(int)mat.total() * mat.channels()];
        	// Aller chercher les octets et les placer dans le vecteur
        	mat.get(0,0,raw);
        	// Créer un objet Serializable qui sera envoyé au serveur
        	SerializableImage img = new SerializableImage(raw, mat.width(), mat.height(), mat.channels(), this.sequence);
			this.sequence++;
			oos.writeObject(img);

        	Thread.sleep(1000/FPS);
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