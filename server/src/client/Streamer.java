package client;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

public class Streamer {
    public static Streamer streamer = null;

    private InputStream in;
    private OutputStream out;
    private DataInputStream dataIn;
    private DataOutputStream dataOut;

    private String streamerName;

    private BufferedImage image;

    /**
     * Utilisation du singleton, afin d'avoir une seule instance d'un Streamer
     *
     * @return l'instance du Streamer
     */
    public static Streamer getInstance() {
        if (streamer == null) streamer = new Streamer();
        return streamer;
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
     * Cette méthode informe le serveur que le client est un streamer
     *
     * @throws IOException
     */
    public void sendType() throws IOException {
        dataOut.writeBoolean(true);
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
        dataOut.writeUTF(streamerName);
    }


    /**
     * Cette méthode permet d'envoyer les images capturé par la webcam au serveur
     *
     * @throws IOException
     */
    public void sendImages() throws IOException {
        //TODO will be removed
        //Webcam webcam = Webcam.getDefault();
        // webcam.open();

        while (true) {
            System.out.println("sending img");

            //image = webcam.getImage();

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            ImageIO.write(image, "PNG", buffer);
            dataOut.writeInt(buffer.size());
            dataOut.flush();
            buffer.close();

            ImageIO.write(image, "PNG", out);
            out.flush();
            out.write('y');
            out.flush();

            while (in.read() != 'y') ;
        }
    }
}
