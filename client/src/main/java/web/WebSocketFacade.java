package web;

import com.google.gson.Gson;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.net.URI;

public class WebSocketFacade extends Endpoint implements MessageHandler.Whole<String> {
    private static final Gson GSON = new Gson();

    private Session session;

    private ServerMessageObserver notificationManager;


    public WebSocketFacade(String url, ServerMessageObserver notificationManager) throws Exception {
        URI uri = new URI("ws://" + url + "/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);
        this.session.addMessageHandler(this);
        this.notificationManager = notificationManager;
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    @Override
    public void onMessage(String s) {
        notificationManager.notify(s);
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }
}