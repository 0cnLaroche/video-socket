package streamer;
import java.awt.FlowLayout;
import java.io.IOException;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Classe qui lance le programme
 */

public class Application {
	
	private static String host = "localhost";
	private final static int PORT = 4523;
	

	public static void main(String[] args) throws Exception {
		
		DummyServer s = new DummyServer();
		s.start();
		
		CaptureVideo cv = new CaptureVideo();
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTextField ipTf = new JTextField("127.0.0.1");
		ipTf.setSize(100, 20);
		JTextField nameTf = new JTextField("Hugo");
		nameTf.setSize(100, 20);
		JLabel ipLb = new JLabel("IP Serveur: ");
		JLabel nameLb = new JLabel("Nom: ");
		JButton btn = new JButton("Connect");
		
		btn.addActionListener((event) -> {
			
			host = ipTf.getText();
			
	        System.out.println("Getting instance");
	        Stream streamer = Stream.getInstance();
	        

	        try {
				streamer.initConnection(host, PORT);
		        System.out.println("Init connection");
				
		        System.out.println("Sending type");
		        streamer.sendType();

		        System.out.println("Setting streamer name");
		        streamer.setStreamerName("Hugo");

		        System.out.println("Sending streaner name");
		        streamer.sendStreamerName();

		        System.out.println("Sending images");
		        
		        cv.setIn(streamer.in);
		        cv.setOut(streamer.out);
				cv.start();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			
		});
		JPanel panel = new JPanel(new FlowLayout());
		
		panel.add(ipLb);
		panel.add(ipTf);
		panel.add(nameLb);
		panel.add(nameTf);
		panel.add(btn);
		
		frame.add(panel);
		
		frame.setSize(400, 200);//400 width and 500 height  
		//frame.setLayout(null);//using no layout managers  
		//frame.pack();
		frame.setVisible(true);//making the frame visible  
		}  
		

	}


