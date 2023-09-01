import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class BotClient
{
    final static int ServerPort = 14001;

    public static void main(String args[]) throws IOException {
        // Get the user to enter their name so that it can be sent as soon as the client has connected to the server.
        String userName = "Magic8Bot";
        InetAddress ip = InetAddress.getByName("localhost");
        Socket socket = new Socket(ip, ServerPort);
        System.out.println(userName + "has successfully connected to the server");
        BotClientMessageSender sender = new BotClientMessageSender(socket, userName); //Create the sender with the user's name.
        BotClientMessageProcessor messageProcessor =  new BotClientMessageProcessor(sender, userName);
        ClientMessageReceiver receiver = new ClientMessageReceiver(socket, messageProcessor);
        new Thread(receiver).start();
    }
}