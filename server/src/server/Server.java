package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Server {
    public  static Server server = null;

    private ServerSocket serverSocket;
    private boolean isOnline;
    private ArrayList<Connection> connections;
    private ArrayList<Streamer> streamers;
    private ArrayList<Viewer> viewers;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Le port est déjà utilisé");
        }
        connections = new ArrayList<>();
        streamers = new ArrayList<>();
        viewers = new ArrayList<>();

        isOnline = true;
    }

    /**
     * Utilisation du singleton, afin d'avoir une seule instance de Serveur
     *
     * @return l'instance du Server
     */
    public static Server getInstance() {
        if (server == null) {
            server = new Server(4523);
        }
        return server;
    }

    /**
     * Cette méthode ajoute un client dans la liste de connection et la maintien à jour
     *
     * @param socket - Soit le socket de connection d'un client
     */
    public void addConnection(Socket socket) {
        System.out.println("Adding new connection");

        Connection connection = null;

        try {
            connections.add(connection = new Connection(socket));

            connection.setConnectionType();

            if(connection.getConnectionType()) {
                addStreamer(connection);
            } else {

                addViewer(connection);
            }
        } catch (Exception e) {
            System.out.println("Une connection a été perdue");
            connections.remove(connection);
        }
    }

    /**
     * Cette méthode ajoute un streamer dans la liste de streamers et la maintien à jour
     *
     * @param connection - les informations relatives aux Input et Output du streamer
     */
    public void addStreamer(Connection connection) {
        System.out.println("Adding streamer");

        Streamer streamer = null;

        try {
            streamer = new Streamer(connection);
            streamer.setStreamerName();

            streamers.add(streamer);

            streamer.start();
            System.out.println("!");
        } catch (Exception e) {
            streamers.remove(streamer);
        }
    }

    /**
     * Cette méthode ajoute une viewer à la liste de viewers et la maintien à jour
     *
     * @param connection - les informations relatives aux Input et Output du viewer
     */
    public void addViewer(Connection connection) {
        System.out.println("Adding viewer");

        Viewer viewer = null;

        try {
            viewer = new Viewer(connection);
            viewers.add(viewer);
            viewer.start();

        } catch (Exception e) {
            viewers.remove(viewer);
        }
    }

    /**
     * Cette méthode retourne une liste de noms des streamers en ligne
     *
     * @return une liste de noms des streamers en ligne
     */
    public ArrayList<Streamer> getStreamers() {
        return streamers;
    }

    public String[] getStreamersNames() {
        String[] streamersNames = new String[streamers.size()];

        for(int i = 0; i < streamers.size(); i++) {
            streamersNames[i] = streamers.get(i).getStreamerName();
        }

        System.out.println(Arrays.toString(streamersNames));

        return streamersNames;
    }

    /**
     * Cette méthode permet au server d'accepter les connections des clients
     */
    public void startServer() {
        System.out.println("The server is now online");

        while (true) {
            try {
                System.out.println("Waiting for client");

                Socket socket = serverSocket.accept();

                addConnection(socket);

            } catch (IOException e) {
                //err avec le server
            }
        }
    }
}
