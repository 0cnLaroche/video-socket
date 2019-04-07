import java.io.IOException;

public class Application {

	public static void main(String[] args) {
        try {
            System.out.println("Getting instance");
            Viewer viewer = Viewer.getInstance();

            System.out.println("Init connection");
            viewer.initConnection("192.168.50.87", 4523);

            System.out.println("Sending type");
            viewer.sendType();

            System.out.println("Receiving streamers names");
            viewer.receiveStreamersNames();

            System.out.println("Sending streamer name");
            viewer.sendStreamerName("Hugo");

            System.out.println("Reveicing images");
            VideoReader reader = new VideoReader(viewer.getIn(), viewer.getOut());
            reader.view();
            
        } catch (IOException e) {
            System.out.println("Une erreur c'est produite... " + e);
        }

	}

}
