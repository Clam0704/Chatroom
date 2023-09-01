import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerClientConnection implements Runnable {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private ServerSocketManager serverSocketManager;
    private String userName = null;
    private boolean running = true;

    public ServerClientConnection(Socket socket, ServerSocketManager serverSocketManager) throws IOException {
        this.socket = socket;
        this.serverSocketManager = serverSocketManager;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ioe) {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            throw ioe;
        }
    }

    public void sendMessage(String message) throws IOException {
        out.writeUTF(message);
        out.flush();
    }

    public void closeConnection() {
        running = false;
    }

    @Override
    public void run() {
        try {
            userName = in.readUTF();
            System.out.println(Thread.currentThread().getName()
                    + " - ServerClientConnection started. User [" + userName + "]");

            while (running) {
                if (in.available() > 0) {
                    String message = in.readUTF();
                    serverSocketManager.broadcastToAllClients("[" + userName + "] " + message);
                }
                Thread.sleep(100); // To prevent calling in.available all the time.
            }
            // Close the socket and streams so nothing is left open.
            socket.close();
            in.close();
            out.close();
            System.out.println(Thread.currentThread().getName() + " - ServerClientConnection closed. " +
                    "User [" + userName + "] disconnected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
