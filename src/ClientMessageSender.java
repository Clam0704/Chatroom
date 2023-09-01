import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientMessageSender implements Runnable {
    private Socket socket;
    private DataOutputStream out;
    // A Scanner with System.in waits when calling nextLine so the thread can't close when the server sends an
    // exit command. Using a BufferedInputStream means available can be called to see if there is anything to read
    // so nothing its not waiting for something to by typed.
    private InputStream sysIn = new BufferedInputStream(System.in);
    private Scanner scn = new Scanner(sysIn);
    private boolean running = true;

    public ClientMessageSender(Socket socket, String userName) throws IOException {
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
        running = false;
    }

    @Override
    public void run() {
        try {
            while (running) {
                if (sysIn.available() > 0) {
                    String msg = scn.nextLine();
                    out.writeUTF(msg);
                }
            }
            socket.close();
            sysIn.close();
            out.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}