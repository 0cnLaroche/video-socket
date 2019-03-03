import java.nio.ByteBuffer;
import javax.swing.JFrame;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

public class CaptureVideo extends Thread {

	private Webcam webcam = Webcam.getDefault();
	private ByteBuffer buffer;
	private boolean paused;
	
	private static final int FPS = 10;
	
	public CaptureVideo() {
		webcam.setViewSize(WebcamResolution.VGA.getSize());
	}
	
	public void mirror() {
		
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
		
		
	}
	
	public void run() {
		
		
		while (!paused) {

			try {

				buffer = webcam.getImageBytes();
		

				// Le délai entre chaque chaque capture
				Thread.sleep(FPS * 10);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public ByteBuffer getBuffer() {
		return this.buffer;
	}
	public void pause() {
		paused = true;
	}
	
	public void resumeCapture() {
		paused = false;
	}


}