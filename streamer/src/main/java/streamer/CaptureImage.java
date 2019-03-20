package streamer;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;

public class CaptureImage {
	
	Webcam webcam;
	
	public CaptureImage() {
		
		webcam = Webcam.getDefault();
		
	}
	
	public void capture() {
		
	
		webcam.open();
		
		try {
			ImageIO.write(webcam.getImage(), "PNG", new File("hello-world.png"));
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		webcam.close();
	}

}