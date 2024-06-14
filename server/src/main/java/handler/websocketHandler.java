package handler;

import json.SerializeUtils;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.io.ConnectionManager;
import service.ServiceUtils;
import websocket.commands.UserGameCommand;

@WebSocket
public class websocketHandler {



//    @OnWebSocketMessage
//    public void onMessage(Session session, String msg) {
//
//
//        try {
//            UserGameCommand command = SerializeUtils.fromJson(message, UserGameCommand.class);
//
//            // Throws a custom UnauthorizedException. Yours may work differently.
//            String username = getUsername(command.getAuthString());
//
//            saveSession(command.getGameID(), session);
//
//            switch (command.getCommandType()) {
//                case CONNECT -> connect(session, username, (ConnectCommand) command);
//                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
//                case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
//                case RESIGN -> resign(session, username, (ResignCommand) command);
//            }
//        } catch (UnauthorizedException ex) {
//            // Serializes and sends the error message
//            sendMessage(session.getRemote(), new ErrorMessage("Error: unauthorized"));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage()));
//        }
//    }



}
