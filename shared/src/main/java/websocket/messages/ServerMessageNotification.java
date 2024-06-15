package websocket.messages;

public class ServerMessageNotification extends ServerMessage{
    String message;
    public ServerMessageNotification(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }

    public String getMessageText() {
        return this.message;
    }

}
