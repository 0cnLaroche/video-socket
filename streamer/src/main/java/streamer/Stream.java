package streamer;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Stream {

	private String HOST = "localhost";
	private int PORT = 8080;
	
	BufferedOutputStream out;
	BufferedReader in;
	Socket socket;
	String sessionName;

	/**
	 * Se connecte au serveur et envois un stream d'images.
	 */
	public Stream(String host, int port) {
		this.HOST = host;
		this.PORT = port;
	}

	public void connect() {

		String fromServer;

		try {
			socket = new Socket(HOST, PORT);

			System.out.println("[STREAMER]: Connecté au serveur");

			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.out = new BufferedOutputStream(socket.getOutputStream());

			PrintWriter pr = new PrintWriter(out);

			// Envoyé le nom du host de la session
			pr.write("session:" + sessionName);

			if ((fromServer = in.readLine()) != sessionName + ":OK") {
				pr.write("Bye.");
				this.close();
				throw new IOException("Pas de confirmation reçu du serveur pour la session " + sessionName);
			}
			System.out.println("[STREAMER]: Session établie avec le serveur. Nom de session : " + sessionName);

			while ((fromServer = in.readLine()) != null) {
				System.out.println(fromServer);
				if (fromServer.equals("Bye."))
					break;
			}
			this.close();


		} catch (IOException e) {
			System.err.println("Echec de la connection avec le serveur à " + HOST + ":" + PORT);
			e.printStackTrace();
		}


	}

	public void close() {

		try {
			out.close();
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	/**
	 * Sets the outputStream. In this project that would come from the VideoCapture Module
	 * @param out
	 */

	public void setOutput(BufferedOutputStream out) {
		this.out = out;
	}
}
