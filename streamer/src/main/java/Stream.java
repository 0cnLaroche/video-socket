import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Stream extends Thread {

	private static final String HOST = "localhost";
	private static final int PORT = 8080;
	
	private CaptureVideo cv;
	
	DataOutputStream out;
	BufferedReader in;
	
	public Stream() {
	}
	
	public void run() {
		
		try {
			Socket socket = new Socket(HOST, PORT);
			
			System.out.println("Connecté au serveur");
			
			String fromServer;
			
			this.in = new BufferedReader(
			        new InputStreamReader(socket.getInputStream()));
			
			this.out = new DataOutputStream(socket.getOutputStream());
			
			while ((fromServer = in.readLine()) != null) {
			    System.out.println("Server: " + fromServer);
			    if (fromServer.equals("Bye."))
			        break;
			    
			}
			
		} catch (UnknownHostException e) {
			System.err.println("La connection a chié");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		
	}

}
