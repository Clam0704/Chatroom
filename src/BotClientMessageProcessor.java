public class BotClientMessageProcessor implements ClientMessageProcessor {

    private BotClientMessageSender botClientMessageSender;
    private String botName;

    public BotClientMessageProcessor(BotClientMessageSender botClientMessageSender, String botName) {
        this.botClientMessageSender = botClientMessageSender;
        this.botName = botName;
    }

    @Override
    public ProcessMessageResponseEnum processMessage(String message) {
        if (message.equals("##EXIT##")) {
            botClientMessageSender.close();
            return ProcessMessageResponseEnum.EXIT;
        } else if (message.startsWith("[" + botName + "]")){
            return ProcessMessageResponseEnum.PROCESSED;
        } else {
            System.out.println(message);
            botClientMessageSender.sendMessage("Hello");
            return ProcessMessageResponseEnum.PROCESSED;
        }
    }
}
