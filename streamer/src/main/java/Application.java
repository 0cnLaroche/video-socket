import java.io.*;

public class Application {

	public static void main(String[] args) throws Exception {
		
		DummyServer server = new DummyServer();
		server.start();

		/*Stream stream = new Stream();
		stream.start();*/

		// Exemple avec piping
		PipedOutputStream pout = new PipedOutputStream();
		DataOutputStream output = new DataOutputStream(pout);

		PipedInputStream pin = new PipedInputStream();
		DataInputStream input = new DataInputStream(pin);

		pin.connect(pout);

		CaptureVideo cv = new CaptureVideo(output);


		//CaptureVideo cv = new CaptureVideo(stream.out);
		cv.start();
		VideoReader vr = new VideoReader(input, 1);
		vr.view();

	}

}
