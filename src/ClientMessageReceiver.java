import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientMessageReceiver implements Runnable {
    private Socket socket;
    private ClientMessageProcessor clientMessageProcessor;
    private DataInputStream in;
    private boolean running = true;

    public ClientMessageReceiver(Socket socket, ClientMessageProcessor clientMessageProcessor) throws IOException {
        this.socket = socket;
        this.clientMessageProcessor = clientMessageProcessor;
        try {
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException ioe) {
            if (in != null) {
                in.close();
            }
            throw ioe;
        }
    }

    @Override
    public void run() {
        try {
            while (running) {
                // read the message sent to this client
                String msg = in.readUTF();
                // Let the message processor process message
                running = clientMessageProcessor.processMessage(msg) != ProcessMessageResponseEnum.EXIT;
            }
            socket.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
