package server;

import java.io.*;
import java.net.Socket;

public class Connection {
    private InputStream in;
    private OutputStream out;
    private DataInputStream dataIn;
    private DataOutputStream dataOut;

    private boolean connectionType;

    public Connection(Socket socket) {
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
            dataIn = new DataInputStream(in);
            dataOut = new DataOutputStream(out);
        } catch (IOException e) {}
    }

    public void setConnectionType() throws  IOException {
        connectionType = dataIn.readBoolean();
    }

    public boolean getConnectionType() {
        return connectionType;
    }

    /**
     *
     * @return
     */
    public InputStream getIn() {
        return in;
    }

    /**
     *
     * @return
     */
    public OutputStream getOut() {
        return dataOut;
    }

    /**
     *
     * @return
     */
    public DataInputStream getDataIn() {
        return dataIn;
    }

    /**
     *
     * @return
     */
    public DataOutputStream getDataOut() {
        return dataOut;
    }
}
