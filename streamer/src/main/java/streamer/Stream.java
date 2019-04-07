package streamer;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;


public class Stream {

	private String HOST = "localhost";
	private int PORT = 8080;
	
	DataOutputStream out;
	DataInputStream in;
	Socket socket;
	
    private String streamerName;
    public static Stream streamer = null;


	/**
	 * Se connecte au serveur et envois un stream d'images.
	 */
	
	public Stream() {
		
	}

	
    /**
     * Cette méthode initialise la connection avec le serveur
     *
     * @param serverIP - soit l'adresse IP du serveur
     * @param port - soit le port utilisé par le serveur
     * @throws IOException
     */
    public void initConnection(String serverIP, int port) throws IOException {
        Socket socket = new Socket(serverIP, port);
        
        HOST = serverIP;
        PORT = port;

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

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

	public void setOutput(DataOutputStream out) {
		this.out = out;
	}
	
	/**
     * Utilisation du singleton, afin d'avoir une seule instance d'un Streamer
     *
     * @return l'instance du Streamer
     */
    public static Stream getInstance() {
        if (streamer == null) streamer = new Stream();
        return streamer;
    }

    /**
     * Cette méthode informe le serveur que le client est un streamer
     *
     * @throws IOException
     */
    public void sendType() throws IOException {
        out.writeBoolean(true);
    }

    /**
     * Cette méthode permet au streamer de choisir son nom
     *
     * @param streamerName - une String étant le nom du choisi
     */
    public void setStreamerName(String streamerName) {
        this.streamerName = streamerName;
    }

    /**
     * Cette méthode permet d'accéder au nom du streamer
     *
     * @return - une String étant le nom du streamer
     * @return - une String étant le nom du streame
     */
    public String getStreamerName() {
        return streamerName;
    }

    /**
     * Cette méthode permet d'envoyer le nom du streamer au serveur
     *
     * @throws IOException
     */
    public void sendStreamerName() throws IOException {
        out.writeUTF(streamerName);
    }


    /**
     * Cette méthode permet d'envoyer les images capturé par la webcam au serveur
     *
     * @throws IOException
     */
    public void sendImages() throws IOException {
    	
    }
 }

