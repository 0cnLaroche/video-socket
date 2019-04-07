package server;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Streamer extends Thread {
    private Connection connection;
    private BufferedImage image;
    private String streamerName;


    public Streamer(Connection connection) {
        this.connection = connection;
        this.image = null;
    }

    /**
     * Cette méthode permet de spécifier le nom du streamer
     *
     * @throws IOException
     */
    public void setStreamerName() throws IOException {
        System.out.println("Setting streamer name");

        streamerName = connection.getDataIn().readUTF();
    }

    /**
     * Cette méthode permet d'avoir l'image envoyé du streamer côté client
     *
     * @return - l'image la plus réçente envoyé par le streamer
     */
    public BufferedImage getImage() {
        return image;
    }




    /**
     * Cette méthode retourne le nom du streamer
     *
     * @return - soit le nom du streamer
     */
    public String getStreamerName() {
        return streamerName;
    }

    /**
     * Cette méthode permet de recevoir les images envoyé par le streamer côté client
     */
    public void receiveImages() throws IOException {
        System.out.println("Receiving images from streamer");

        while (true) {
            byte[] buffer = new byte[connection.getDataIn().readInt()];
            connection.getDataIn().readFully(buffer);

            while (connection.getIn().read() != 'y') ;
            connection.getOut().write('y');
            connection.getOut().flush();

            InputStream inStream = new ByteArrayInputStream(buffer);
            image = ImageIO.read(inStream);
            inStream.close();
        }
    }

    @Override
    public void run() {
        System.out.println("Streamer is running");

        try {
            receiveImages();
        } catch (IOException e) {
            //err
        }
    }
}
