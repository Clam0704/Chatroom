public class ClientConsoleMessageProcessor implements ClientMessageProcessor {

    private ClientMessageSender clientMessageSender;

    public ClientConsoleMessageProcessor(ClientMessageSender clientMessageSender) {
        this.clientMessageSender = clientMessageSender;
    }

    @Override
    public ProcessMessageResponseEnum processMessage(String message) {
        if (message.equals("##EXIT##")) {
            clientMessageSender.close();
            return ProcessMessageResponseEnum.EXIT;
        } else {
            System.out.println(message);
            return ProcessMessageResponseEnum.PROCESSED;
        }
    }
}
