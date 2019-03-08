import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

public class Client {
    private Webcam webcam;

    private Socket socket;
    private String server;
    private int port;

    private OutputStream out;
    private InputStream in;
    private DataOutputStream dataOut;
    private BufferedImage bufferedImage;

    private boolean isOnline;

    public Client(String server, int port) {
        this.webcam = Webcam.getDefault();
        this.server = server;
        this.port = port;
    }

    /**
     * Cette méthode permet au client de se connecter au serveur
     */
    public void connectToServer() {
        try {
            socket = new Socket(server, port);

            out = socket.getOutputStream();
            in = socket.getInputStream();
            dataOut = new DataOutputStream(out);

            isOnline = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cette méthode permet au client d'envoyer les images de sa webcam au serveur
     */
    public void streaming() {
        this.webcam.open();

        try {
            while (isOnline) {
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                bufferedImage = webcam.getImage();

                //Permet d'envoyer la taille de l'image au serveur
                ImageIO.write(bufferedImage, "PNG", byteOut);
                dataOut.writeInt(byteOut.size());
                dataOut.flush();
                byteOut.close();

                //Permet d'envoyer l'image au serveur
                ImageIO.write(bufferedImage, "PNG", out);
                out.flush();
                out.write('y');
                out.flush();

                while (in.read() != 'y') ;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client c = new Client("localhost", 3030);
        c.connectToServer();
        c.streaming();
    }
}
