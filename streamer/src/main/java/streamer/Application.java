package streamer;
import java.io.*;
import java.net.URI;
import java.net.URL;

import viewer.VideoReader;

/**
 * Classe qui lance le programme
 */

public class Application {

	public static void main(String[] args) throws Exception {
		// NOTE
		// Le host + p
		DummyServer server = new DummyServer();
		server.start();

		URI url = new URI("blank://" + args[0]);

		Stream stream = new Stream(url.getHost(), url.getPort());
		stream.connect();


		// Exemple avec piping
		PipedOutputStream pout = new PipedOutputStream();
		BufferedOutputStream output = new BufferedOutputStream(pout);

		PipedInputStream pin = new PipedInputStream();
		BufferedInputStream input = new BufferedInputStream(pin);

		pin.connect(pout);

		CaptureVideo cv = new CaptureVideo(output);


		//CaptureVideo cv = new CaptureVideo(stream.out);
		cv.start();
		VideoReader vr = new VideoReader(input);
		vr.view();

	}

}
