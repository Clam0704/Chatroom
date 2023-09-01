import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class ServerSocketManager implements Runnable {
    private List<ServerClientConnection> serverClientConnectionList = new ArrayList<>();
    private ServerSocket serverSocket;
    private int port;
    private boolean running = true;

    public ServerSocketManager(int port) throws IOException {
        this.port = port;
        serverSocket = new ServerSocket(port);
    }

    public void broadcastToAllClients(String message) throws IOException {
        synchronized (serverClientConnectionList) {
            List<ServerClientConnection> forRemoval = new ArrayList<>();
            for (ServerClientConnection serverClientConnection : serverClientConnectionList) {
                try {
                    serverClientConnection.sendMessage(message);
                } catch (SocketException se) {
                    // Must be dead so mark it
                    forRemoval.add(serverClientConnection);
                    System.out.println("Removing dead connection");
                }
            }
            for (ServerClientConnection serverClientConnection : forRemoval) {
                serverClientConnectionList.remove(serverClientConnection);
            }
        }
    }

    public void close() throws IOException {
        running = false;
        serverSocket.close(); // This will cause the serverSocket.accept() to throw an exception
        synchronized (serverClientConnectionList) {
            for (ServerClientConnection serverClientConnection : serverClientConnectionList) {
                serverClientConnection.closeConnection();
            }
        }
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " - ServerSocket listening on port " + port);
        try {
            while (running) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("Received a new client request : " + socket);
                    ServerClientConnection serverClientConnection = new ServerClientConnection(socket, this);
                    synchronized (serverClientConnectionList) {
                        serverClientConnectionList.add(serverClientConnection);
                    }
                    new Thread(serverClientConnection).start();
                } catch (SocketException se) {
                    // SocketException was thrown by serverSocket.close().
                    if (running) {
                        se.printStackTrace();
                    }
                }
            }
        } catch (IOException ioe) {
            // Catch IOException outside while loop to prevent looping with an error forever.
            ioe.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " - ServerSocket closed");
    }
}