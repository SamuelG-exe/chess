package websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;


public class ConnectionTracker {
    public String token;
    public Session session;

    public ConnectionTracker(String token, Session session) {
        this.token = token;
        this.session = session;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}
