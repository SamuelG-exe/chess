package websocket;

import json.SerializeUtils;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;

import java.util.*;

@WebSocket
public class websocketHandler {

    private Map<String, Set<ConnectionTracker>> gameWithUsers = new HashMap<>();



    @OnWebSocketMessage
    public void onMessage(Session session, String jSONFromClient) {


        try {
            UserGameCommand outerCommand = SerializeUtils.fromJson(jSONFromClient, UserGameCommand.class);
            String jsonOfOuterCommand=SerializeUtils.toJson(outerCommand);
            UserGameCommand command = SerializeUtils.fromJson(jsonOfOuterCommand, UserGameCommand.class);

            // Throws a custom UnauthorizedException. Yours may work differently.
            String token = command.getAuthString();
            String gameID = command.getGame().gameID();

            if(gameWithUsers.containsKey(gameID)){
                Set<ConnectionTracker> users = gameWithUsers.get(gameID);
                users.add(new ConnectionTracker(token, session));
                gameWithUsers.put(gameID, users);
            }
            else{
                Set<String> users = new HashSet<>();
                users.add(token);
                gameWithUsers.put(gameID, users);
            }
            switch (command.getCommandType()) {
                case CONNECT -> connect(session, command);
                case MAKE_MOVE -> makeMove(session, command);
                case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
                case RESIGN -> resign(session, username, (ResignCommand) command);
            }
        } catch (UnauthorizedException ex) {
            // Serializes and sends the error message
            sendMessage(session.getRemote(), new ErrorMessage("Error: unauthorized"));
        } catch (Exception ex) {
            ex.printStackTrace();
            sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage()));
        }


    }
    private void connect(Session session,UserGameCommand command){
        Set<String> users = gameWithUsers.get(command.getGame().gameID());
        for()
    }


}
