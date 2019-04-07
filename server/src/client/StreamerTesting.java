package client;

import java.io.IOException;

public class StreamerTesting {
    public static void main(String[] args) {
        try {
            System.out.println("Getting instance");
            Streamer streamer = Streamer.getInstance();

            System.out.println("Init connection");
            streamer.initConnection("7.126.255.122", 4523);

            System.out.println("Sending type");
            streamer.sendType();

            System.out.println("Setting streamer name");
            streamer.setStreamerName("Hugo");

            System.out.println("Sending streaner name");
            streamer.sendStreamerName();

            System.out.println("Sending images");
            streamer.sendImages();
        } catch (IOException e) {
            System.out.println("Une erreur c'est produite... " + e);
        }
    }
}
