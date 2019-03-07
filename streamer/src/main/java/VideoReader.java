import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.PriorityQueue;
// Pas certain si fait une différence d'utilisé un BlockingQueue ou non.
import java.util.concurrent.PriorityBlockingQueue;

public class VideoReader extends JFrame {

    private static final int CAPACITY = 10;
    private static final int FPS = 30;
    private int width;
    private int height;
    private JPanel contentPane;
    private PriorityQueue<SerializableImage> queue;
    private ImageConverter converter;
    private boolean paused;

    /**
     * Reçois un stream de données, convertie en images, place dans le bon ordre et affiche à l'écran
     * @param in
     * @throws IOException
     */


    public VideoReader(BufferedInputStream in) throws IOException {

        this.queue = new PriorityQueue<SerializableImage>(CAPACITY);
        this.converter = new ImageConverter();
        this.width = 1280;
        this.height = 720;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, width, height);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);
        contentPane.setLayout(null);



        new Thread(new Runnable() {
            @Override
            public void run() {
                try(ObjectInputStream ois = new ObjectInputStream(in)) {
                    SerializableImage img;

                    while (true) {
                        if ((img = (SerializableImage) ois.readObject()) != null) {

                            // Si la file est pleine, on jete la prochaine image qui devait être lue
                            // pour ne pas créer de délais
                            if (queue.size() >= CAPACITY) {
                                queue.poll();
                            }

                            // queue.put(img);
                            queue.add(img);

                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();



    }

    public void paint(Graphics g){

        if(queue.peek() != null) {

            BufferedImage img = converter.getImage(queue.poll());
            g = contentPane.getGraphics();
            g.drawImage(img, 0, 0, this);

        }


    }

    public void view() {

        this.setVisible(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!paused) {

                    repaint();
                    try {
                        Thread.sleep(1000/FPS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        }).start();

    }

    public synchronized void pause() {
        this.paused = true;
    }

    public synchronized void resume() {
        this.paused = false;
    }

    @Override
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
