package websocket.messages;

public interface ServerMessageObserver {
    public default void notify(ServerMessage message){}
}
