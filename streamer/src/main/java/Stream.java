import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import com.github.sarxos.webcam.Webcam;

public class Stream extends Thread {

	private static final String HOST = "localhost";
	private static final int PORT = 8080;
	
	private CaptureVideo cv;
	
	DataOutputStream out;
	BufferedReader in;
	
	public Stream(CaptureVideo cv) {
		this.cv = cv;
	}
	
	public void run() {
		
		try {
			Socket socket = new Socket(HOST, PORT);
			
			String fromServer;
			
			this.in = new BufferedReader(
			        new InputStreamReader(socket.getInputStream()));
			
			this.out = new DataOutputStream(socket.getOutputStream());
			
			new OutputStreamWriter(socket.getOutputStream());
	
			
			while ((fromServer = in.readLine()) != null) {
			    System.out.println("Server: " + fromServer);
			    if (fromServer.equals("Bye."))
			        break;
				if(cv.getBuffer() != null) {
					out.write(cv.getBuffer().array());
				}
			    
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		
	}

}
