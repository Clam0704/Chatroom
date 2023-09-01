import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient
{
    final static int ServerPort = 14001;

    public static void main(String args[]) throws IOException {
        // Get the user to enter their name so that it can be sent as soon as the client has connected to the server.
        System.out.print("Enter you username: ");
        String userName = new Scanner(System.in).nextLine();
        InetAddress ip = InetAddress.getByName("localhost");
        Socket socket = new Socket(ip, ServerPort);
        System.out.println("Hi " + userName + ", you have successfully connected to the server");
        ClientMessageSender sender = new ClientMessageSender(socket, userName); //Create the sender with the user's name.
        ClientConsoleMessageProcessor messageProcessor =  new ClientConsoleMessageProcessor(sender);
        ClientMessageReceiver receiver = new ClientMessageReceiver(socket, messageProcessor);
        new Thread(sender).start();
        new Thread(receiver).start();
    }
}