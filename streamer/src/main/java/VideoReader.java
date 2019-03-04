
import org.opencv.core.Core;
import org.opencv.core.Mat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.PriorityBlockingQueue;

public class VideoReader extends JFrame {

    private static final int CAPACITY = 10;
    private JPanel contentPane;

    private PriorityBlockingQueue<SerializableImage> queue;

    private double gamma;

    private ImageConverter converter;


    public VideoReader(DataInputStream in, double gamma) throws IOException {

        this.queue = new PriorityBlockingQueue<SerializableImage>(CAPACITY);
        this.converter = new ImageConverter();
        this.gamma = gamma;

        ObjectInputStream ois = new ObjectInputStream(in);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, 1280, 720);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);
        contentPane.setLayout(null);



        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SerializableImage img;

                    while (true) {
                        if ((img = (SerializableImage) ois.readObject()) != null) {

                            if (queue.size() >= CAPACITY) {
                                queue.poll();
                            }

                            queue.put(img);


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
                while(true) {

                    repaint();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        }).start();

    }

}
