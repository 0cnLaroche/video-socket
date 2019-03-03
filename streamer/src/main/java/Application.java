

public class Application {

	public static void main(String[] args) {
		
		CaptureVideo cv = new CaptureVideo();
		
		Stream streamer = new Stream(cv);
		
		streamer.start();
		
		cv.start();
		cv.mirror();
		
		

	}

}
