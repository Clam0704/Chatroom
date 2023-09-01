import java.io.IOException;
import java.util.Scanner;

// ChatServer class
public class ChatServer {

    public void startServer(int port) throws IOException {
        ServerSocketManager serverSocketManager = new ServerSocketManager(port);
        new Thread(serverSocketManager).start();

        Scanner scn = new Scanner(System.in);
        boolean exit = false;
        do {
            System.out.print("Enter command: ");
            String command = scn.nextLine();
            if (command.equals("EXIT")) {
                exit = true;
            } else {
                System.out.println("Command not recognised");
            }
        } while (!exit);
        serverSocketManager.broadcastToAllClients("##EXIT##");
        serverSocketManager.close();
    }

    public static void main(String[] args) throws Exception {
        int port = 14001;
        if (args.length > 0) {
            if (args.length == 2 && args[0].equals("-csp")) {
                port = Integer.parseInt(args[1]);
            } else {
                throw new IllegalArgumentException("Illegal arguments passed");
            }
        }
        new ChatServer().startServer(port);
    }
}