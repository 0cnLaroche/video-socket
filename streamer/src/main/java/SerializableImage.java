

import java.io.Serializable;

/**
 * Classe image qui sera utilisé par les clients. Doit être Serializable pour utilisé un ObjectOutputStream et
 * ObjectInputStream.
 * @author samuel
 */


public class SerializableImage implements Serializable, Comparable<SerializableImage> {

    private static final long serialVersionUID = 7040956247022451242L;
    //private transient final IplImage img;
    private byte[] raw;
    private int width;
    private int height;
    private int channels;
    private int sequence;


    public SerializableImage(byte[] raw, int width, int height, int channels, int sequence) {
        this.raw = raw;
        this.width = width;
        this.height = height;
        this.channels = channels;
        this.sequence = sequence;
    }

    public int compareTo(SerializableImage o) {
        return ((int)this.sequence - o.sequence);
    }

    public byte[] getRaw() {
        return raw;
    }

    public void setRaw(byte[] raw) {
        this.raw = raw;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getChannels() {
        return channels;
    }

    public void setChannels(int channels) {
        this.channels = channels;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
