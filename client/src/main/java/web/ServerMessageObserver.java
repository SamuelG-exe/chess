package web;

import websocket.messages.ServerMessage;

public interface ServerMessageObserver {
    public void notify(String message);
}
