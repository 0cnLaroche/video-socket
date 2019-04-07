
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ArrayBlockingQueue;

public class VideoReader extends JFrame {

    private static final int CAPACITY = 10;
    private static final int FPS = 30;
    private int width;
    private int height;
    private JPanel contentPane;
    private ArrayBlockingQueue<BufferedImage> queue;
    private boolean paused;

    /**
     * Reçois un stream de données, convertie en images, place dans le bon ordre et affiche à l'écran
     * @param in
     * @throws IOException
     */


    public VideoReader(DataInputStream in, DataOutputStream out) throws IOException {

        this.queue = new ArrayBlockingQueue<BufferedImage>(CAPACITY);
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
                try {
                    //SerializableImage img;

                    while (true) {
                    	
                        byte[] buffer = new byte[in.readInt()];
                        in.readFully(buffer);

                        while (in.read() != 'y') ;
                        out.write('y');
                        out.flush();

                        InputStream inputStream = new ByteArrayInputStream(buffer);
                       
                        BufferedImage image = ImageIO.read(inputStream);
                        // Si la file est pleine, on jete la prochaine image qui devait être lue
                        // pour ne pas créer de délais
                        if (queue.size() >= CAPACITY) {
                            queue.poll();
                        }

                        // queue.put(img);
                        queue.add(image);
                        inputStream.close();
                    	

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }
        }).start();



    }

    public VideoReader() {
		// TODO Auto-generated constructor stub
	}

	public void paint(Graphics g){

        if(queue.peek() != null) {

            BufferedImage img = queue.poll();
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
