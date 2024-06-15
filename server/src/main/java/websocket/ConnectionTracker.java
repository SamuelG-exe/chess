package websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.Objects;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConnectionTracker that)) return false;
        return Objects.equals(token, that.token) && Objects.equals(session, that.session);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, session);
    }
}
