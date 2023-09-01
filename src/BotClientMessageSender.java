import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

public class BotClientMessageSender {
    private Socket socket;
    private DataOutputStream out;

    public BotClientMessageSender(Socket socket, String userName) throws IOException {
        this.socket = socket;
        try {
            // Open the output stream and send the user's name immediately.
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(userName);
        } catch (IOException ioe) {
            if (out != null) {
                out.close();
            }
            throw ioe;
        }
    }

    public void close() {
        try {
            out.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void sendMessage(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}