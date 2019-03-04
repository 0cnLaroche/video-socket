import java.io.*;
import java.nio.ByteBuffer;

import javax.swing.JFrame;


import org.opencv.core.Core;
import org.opencv.core.Mat;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import org.opencv.videoio.VideoCapture;

public class CaptureVideo extends Thread {

	//private FrameGrabber grabber;
	private ByteBuffer buffer;
	private DataOutputStream out;
	private boolean paused;
	private VideoCapture vc;

	static{
		//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		nu.pattern.OpenCV.loadShared();
	}
	
	private static final int FPS = 10;
	
	public CaptureVideo(DataOutputStream out) throws Exception {
		vc = new VideoCapture();
		// grabber = FrameGrabber.createDefault(0);
		this.out = out;
	}
	
	/*public void mirror() {
		
		WebcamPanel panel = new WebcamPanel(webcam);
		
		panel.setFPSDisplayed(true);
		panel.setDisplayDebugInfo(true);
		panel.setImageSizeDisplayed(true);
		panel.setMirrored(true);
		
		JFrame window = new JFrame("Mirror");
		window.add(panel);
		window.setResizable(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		
	}*/
	
	public void run() {
		
		try {
			captureCV(out);
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
		paused = true;
	}
	
	public void resumeCapture() {
		System.out.println("Capture vidéo reprise");
		paused = false;
	}
	
	/*private synchronized void captureWebcam() {

		System.out.println("Démarre la capture...");

		while (!paused) {

			try {

				buffer = webcam.getImageBytes();
				System.out.println("string " + new String(buffer.array(), "US-ASCII"));

				// Le délai entre chaque chaque capture
				Thread.sleep(FPS * 10);

			} catch (InterruptedException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

	}*/
	
	private synchronized void captureCV(DataOutputStream out) throws IOException, InterruptedException {
		
		// Créer une instance de la webcam par defaut

        ObjectOutputStream oos = new ObjectOutputStream(out);
        // Numéro de séquence
        int sequence = 0;

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
        	SerializableImage img = new SerializableImage(raw, mat.width(), mat.height(), mat.channels(), sequence);
			sequence++;
			oos.writeObject(img);

        	Thread.sleep(100);
        }

        System.out.println("Capture Vidéo terminé \nFrames envoyés: " + sequence);

	}

	/*public double getGamma() {
		return this.grabber.getGamma();
	}*/


}