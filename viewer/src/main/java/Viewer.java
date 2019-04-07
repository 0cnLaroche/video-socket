

import java.io.*;
import java.net.Socket;

public class Viewer {
    public static Viewer viewer = null;

    private DataInputStream dataIn;
    private DataOutputStream dataOut;
    private InputStream in;
    private OutputStream out;

    /**
     * Utilisation du singleton, afin d'avoir une seule instance du Viewer
     *
     * @return l'instance du Viewer
     */
    public static Viewer getInstance() {
        if (viewer == null) viewer = new Viewer();
        return viewer;
    }
    
    public DataInputStream getIn() {
    	return this.dataIn;
    }
    public DataOutputStream getOut() {
    	return this.dataOut;
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

        in = socket.getInputStream();
        out = socket.getOutputStream();
        dataIn = new DataInputStream(in);
        dataOut = new DataOutputStream(out);
    }

    /**
     * Cette méthode informe le serveur que le client est un viewer
     *
     * @throws IOException
     */
    public void sendType() throws IOException {
        dataOut.writeBoolean(false);
    }

    /**
     * Cette méthode permet de recevoir les noms des streamers en ligne du serveur
     *
     * @return - un tableau contenant les noms des streamers en ligne
     */
    public String[] receiveStreamersNames() throws IOException {
        String[] streamersName = null;

        String s = dataIn.readUTF();
        s = s.replace("[", "").replace(",", "").replace("]", "");

        streamersName = s.split(" ");

        return streamersName;
    }

    /**
     * Cette méthode permet d'envoyer au serveur le nom du streamer choisi par le client
     *
     * @param streamerName - soit le nom du streamer choisi
     * @throws IOException
     */
    public void sendStreamerName(String streamerName) throws IOException {
        dataOut.writeUTF(streamerName);
    }


}
