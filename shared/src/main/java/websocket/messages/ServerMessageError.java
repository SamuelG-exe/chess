package websocket.messages;

public class ServerMessageError extends ServerMessage{

    String errorMessage;
    public ServerMessageError(String message) {
        super(ServerMessageType.ERROR);
        this.errorMessage= message;
    }
    public String getMessageText() {
        return this.errorMessage;
    }
}
