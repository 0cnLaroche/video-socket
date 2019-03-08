import javax.swing.*;
import java.awt.*;

public class ShowImage {
    JFrame frame;
    JLabel label;
    ImageIcon image;

    public ShowImage() {
        frame = new JFrame("No title yet bro");
        label = new JLabel();
        image = new ImageIcon();
    }

    public void startImage() {
        frame.setSize(400, 400);
        frame.setVisible(true);
    }

    public  void refresh(Image img) {
        image.setImage(img);
        label.setIcon(image);
        label.revalidate();
        label.repaint();
        frame.add(label);
        frame.revalidate();
        frame.repaint();
    }

}
