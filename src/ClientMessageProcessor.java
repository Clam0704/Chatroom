public interface ClientMessageProcessor {

    /**
     *
     * @param message message to process
     * @return the appropriate response code.
     */
    ProcessMessageResponseEnum processMessage(String message);
}
