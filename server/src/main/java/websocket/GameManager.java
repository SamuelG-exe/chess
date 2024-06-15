package websocket;

import json.SerializeUtils;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GameManager {
    public final ConcurrentHashMap<String, Set<ConnectionTracker>> gameWithUsers = new ConcurrentHashMap<>();

    public void add(String gameID, String token, Session session) {
        if (gameWithUsers.containsKey(gameID)) {
            Set<ConnectionTracker> users = gameWithUsers.get(gameID);
            users.add(new ConnectionTracker(token, session));
            gameWithUsers.put(gameID, users);
        } else {
            Set<ConnectionTracker> users = new HashSet<>();
            users.add(new ConnectionTracker(token, session));
            gameWithUsers.put(gameID, users);
        }
    }

    public void remove(String gameID, String token) {
        Set<ConnectionTracker> users = gameWithUsers.get(gameID);
        ConnectionTracker userToBeRemoved = null;
        for (ConnectionTracker user : users) {
            if (Objects.equals(user.token, token)) {
                userToBeRemoved = user;
            }
        }
        users.remove(userToBeRemoved);
        gameWithUsers.put(gameID, users);
    }


    public void broadcast(String gameID, String currentToken, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<ConnectionTracker>();
        Set<ConnectionTracker> users = gameWithUsers.get(gameID);
        for (ConnectionTracker user : users) {
            if (user.session.isOpen()) {
                if (!user.token.equals(currentToken)) {
                    ///turn into json
                    user.send(SerializeUtils.toJson(notification));
                }
            } else {
                removeList.add(user);
            }
        }


        // Clean up any connections that were left open.
        for (var oldConnection : removeList) {
            users.remove(oldConnection);
            gameWithUsers.put(gameID, users);
        }
    }

    public void broadcastAll(String gameID, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<ConnectionTracker>();
        Set<ConnectionTracker> users = gameWithUsers.get(gameID);
        for (ConnectionTracker user : users) {
            if (user.session.isOpen()) {
                    user.send(SerializeUtils.toJson(notification));
                }
            else {
                removeList.add(user);
            }
        }
        // Clean up any connections that were left open.
        for (var oldConnection : removeList) {
            users.remove(oldConnection);
            gameWithUsers.put(gameID, users);
        }
    }


}
