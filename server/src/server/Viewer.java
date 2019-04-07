package server;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class Viewer extends Thread {
    public static Viewer viewer = null;

    private Connection connection;
    private Streamer streamer;
    private BufferedImage image;

    public Viewer(Connection connection) {
        this.connection = connection;
    }

    /**
     * Cette méthode envoie au viewer la liste des streamers présentement en ligne
     */
    public void sendStreamersNames() throws IOException {
        connection.getDataOut().writeUTF(Arrays.toString(Server.getInstance().getStreamersNames()));
    }

    /**
     * Cette méthode reçoit le nom du streamer qui le viewer à choisi
     */
    public void receiveStreamerName() throws IOException {
        String streamerName = connection.getDataIn().readUTF();

        for (Streamer streamer : Server.getInstance().getStreamers()) {
            if (streamer.getStreamerName().equals(streamerName)) {
                this.streamer = streamer;
            } else {
                sendStreamersNames();
            }
        }
    }

    /**
     * Cette méthode envoie les images du streamer au viewer
     */
    public void sendImages() throws IOException {
        System.out.println("Sending images to viewer");

        while (true) {
            image = streamer.getImage();

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            ImageIO.write(image, "PNG", buffer);
            connection.getDataOut().writeInt(buffer.size());
            connection.getDataOut().flush();
            buffer.close();

            ImageIO.write(image, "PNG", connection.getOut());
            connection.getOut().flush();
            connection.getOut().write('y');
            connection.getOut().flush();

            while (connection.getIn().read() != 'y') ;
        }
    }

    @Override
    public void run() {
        System.out.println("Viewer is running");

        try {
            System.out.println("sending names");
            sendStreamersNames();
            System.out.println("receiving names");
            receiveStreamerName();
            System.out.println("sending images");
            sendImages();
        } catch (IOException e) {
            //err
        }
    }
}
