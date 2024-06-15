package web;

import com.google.gson.Gson;
import websocket.messages.ServerMessage;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

public class WebSocketFacade extends Endpoint {
    private static final Gson GSON = new Gson();
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void onMessage(String jSonMessage) {
        try {
            ServerMessage message = GSON.fromJson(jSonMessage, ServerMessage.class);
            this.observer.notify(message);
        } catch(Exception ex) {
            this.observer.notify(new Error(ex.getMessage()));
        }
    }

}
