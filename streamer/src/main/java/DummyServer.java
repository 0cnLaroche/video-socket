import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Serveur Dummy utilisé pour testing seulement
 */

public class DummyServer extends Thread {

	public void run() {
		
		try ( 
				ServerSocket socket = new ServerSocket(8080);
			    Socket clientSocket = socket.accept();
			    PrintWriter out =
			        new PrintWriter(clientSocket.getOutputStream(), true);

			    BufferedReader in = new BufferedReader(
			        new InputStreamReader(clientSocket.getInputStream()));
			) {
				
			out.println("Hi this is Dummy Server");

			
			
		} catch (IOException e) {

			e.printStackTrace();
		}
		
	}

}
