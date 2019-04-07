package streamer;
import java.net.URI;

/**
 * Classe qui lance le programme
 */

public class Application {

	public static void main(String[] args) throws Exception {
		// NOTE
		// Le host + p
		
		URI url = new URI("blank://" + args[0]);
		
		CaptureVideo cv;

		// Exemple avec piping
		/* 
		PipedOutputStream pout = new PipedOutputStream();
		BufferedOutputStream output = new BufferedOutputStream(pout);

		PipedInputStream pin = new PipedInputStream();
		BufferedInputStream input = new BufferedInputStream(pin);

		pin.connect(pout);
		*/

		


		//CaptureVideo cv = new CaptureVideo(stream.out);
		
        System.out.println("Getting instance");
        Stream streamer = Stream.getInstance();
        

        System.out.println("Init connection");
        streamer.initConnection(url.getHost(), url.getPort());

        System.out.println("Sending type");
        streamer.sendType();

        System.out.println("Setting streamer name");
        streamer.setStreamerName("Hugo");

        System.out.println("Sending streaner name");
        streamer.sendStreamerName();

        System.out.println("Sending images");
        cv = new CaptureVideo(streamer.out, streamer.in);
		cv.start();
		
		//VideoReader vr = new VideoReader(input);
		//vr.view();

	}

}
