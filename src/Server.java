import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private boolean isOnline;

    private OutputStream out;
    private InputStream in;
    private DataInputStream dataIn;

    public Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            this.isOnline = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cette méthode permet de recevoir les images envoyé par le client.
     * Un nouveau Thread est créé pour chaque client
     */
    private void gettingImage() {
        new Thread() {

            BufferedImage buffImage = null;

            public void run() {
                try {
                    //ShowImage is only for demo :)
                    ShowImage showImage = new ShowImage();
                    showImage.startImage();

                    while(isOnline) {
                        int imageSize = dataIn.readInt();
                        byte[] buffer = new byte[imageSize];
                        dataIn.readFully(buffer);

                        while (in.read() != 'y') ;
                        out.write('y');
                        out.flush();

                        InputStream inStream = new ByteArrayInputStream(buffer);
                        buffImage = ImageIO.read(inStream);
                        inStream.close();

                        showImage.refresh(buffImage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * Cette méthode initialise le serveur et attend un client
     */
    public void startServer() {
        Socket socket;

        while(isOnline) {
            try {
                socket = serverSocket.accept();

                out = socket.getOutputStream();
                in = socket.getInputStream();
                dataIn = new DataInputStream(in);

            } catch(IOException e) {
                e.printStackTrace();
            }
            gettingImage();
        }
    }

    public static void main(String[] args) {
        Server s = new Server(3030);

        s.startServer();
    }
}
