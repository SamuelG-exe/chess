package websocket;

import chess.ChessGame;
import chess.ChessMove;
import dataaccess.DataAccessException;
import dataaccess.dao.AuthDAO;
import dataaccess.dao.GameDAO;
import dataaccess.dao.UserDAO;
import json.SerializeUtils;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
import websocket.messages.ServerMessageError;
import websocket.messages.ServerMessageLoadGme;
import websocket.messages.ServerMessageNotification;

import java.io.IOException;
import java.util.Objects;

import static websocket.messages.ServerMessage.ServerMessageType.*;

@WebSocket
public class WebsocketHandler {

    private final GameManager gameManager = new GameManager();
    private AuthDAO authTokensDAO;
    private GameDAO gameDAO;

    public WebsocketHandler(UserDAO users, AuthDAO authTokens, GameDAO games) {
        this.authTokensDAO = authTokens;
        this.gameDAO = games;
    }


    @OnWebSocketMessage
    public void onMessage(Session session, String jSONFromClient) {


        try {
            UserGameCommand command = SerializeUtils.fromJson(jSONFromClient, UserGameCommand.class);

            // Throws a custom UnauthorizedException. Yours may work differently.

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, command);
                case MAKE_MOVE -> makeMove(session, SerializeUtils.fromJson(jSONFromClient, MakeMoveCommand.class));
                case LEAVE -> leaveGame(session, command);
                case RESIGN -> resign(session, command);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void connect(Session session,UserGameCommand command) throws DataAccessException, IOException {

        String token = command.getAuthString();
        String gameID = command.getGameID();

        GameData currntGame = gameDAO.getGame(gameID);
        String whiteUsername = currntGame.whiteUsername();
        String blackUsername = currntGame.blackUsername();


        AuthData currntUser= authTokensDAO.getAuth(token);
        String userName = currntUser.username();

        gameManager.add(gameID, token, session);

        String message;

        if(blackUsername != null && blackUsername.equals(userName)){
            message = String.format("%s has joined the Game as Team Black", currntUser.username());
        } else if (whiteUsername != null && whiteUsername.equals(userName)) {
            message = String.format("%s has joined the Game as Team White", currntUser.username());

        }else{
            message = String.format("%s has joined the Game as an Observer", currntUser.username());
        }
        session.getRemote().sendString(SerializeUtils.toJson(new ServerMessageLoadGme(currntGame)));


        var notification = new ServerMessageNotification(message);
        gameManager.broadcast(gameID, token, notification);

    }

    private void leaveGame(Session session, UserGameCommand command) throws DataAccessException, IOException {
        String token = command.getAuthString();
        String gameID = command.getGameID();

        AuthData currntUser= authTokensDAO.getAuth(token);
        GameData currntGame = gameDAO.getGame(gameID);

        if(currntGame.whiteUsername() == currntUser.username()){
            GameData newCurrntGame = new GameData(currntGame.gameID(), null, currntGame.blackUsername(), currntGame.gameName(), currntGame.game());
            gameDAO.updateGame(gameID, newCurrntGame);
        }

        gameManager.remove(gameID, token);
        var message = String.format("%s has left the Game", currntUser.username());
        var notification = new ServerMessageNotification(message);
        gameManager.broadcast(gameID, token, notification);

    }

    private void makeMove(Session session, MakeMoveCommand command) throws Exception {
        ChessMove move = command.getMove();
        String token = command.getAuthString();
        String gameID = command.getGameID();


        AuthData currntUser= authTokensDAO.getAuth(token);
        GameData currntGame = gameDAO.getGame(gameID);

        try {
            okToMoveChecker(currntGame, currntUser, move);
        }catch (Exception e){
            String message = SerializeUtils.toJson(new ServerMessageError(e.getMessage()));
            session.getRemote().sendString("WebSocket response: " + message);
        }

        var message = String.format("%s has made a Move", currntUser.username());
        var notification = new ServerMessageNotification(message);
        gameManager.broadcast(gameID, token, notification);

    }

    private void resign(Session session, UserGameCommand command) throws DataAccessException, IOException {
        String token = command.getAuthString();
        String gameID = command.getGameID();

        AuthData currntUser= authTokensDAO.getAuth(token);
        GameData currntGame = gameDAO.getGame(gameID);

        currntGame.game().setGameOver(true);

        gameDAO.updateGame(gameID, currntGame);

        var message = String.format("%s has resigned", currntUser.username());
        var notification = new ServerMessageNotification(message);
        gameManager.broadcast(gameID, token, notification);

    }

    private void okToMoveChecker(GameData game, AuthData currntUser, ChessMove move) throws Exception {
        String userName = currntUser.username();
        String whiteUserName = game.whiteUsername();
        String blackUserName = game.blackUsername();
        ChessGame.TeamColor turn = game.game().getTeamTurn();
        ChessGame.TeamColor currentUsersTeam;

        if (Objects.equals(userName, whiteUserName)) {
            userName=whiteUserName;
            currentUsersTeam = ChessGame.TeamColor.WHITE;
        }
        else if (Objects.equals(userName, blackUserName)) {
            userName=blackUserName;
            currentUsersTeam = ChessGame.TeamColor.BLACK;
        }
        else{
            throw new Exception(" Not a player in the game ");
        }

        var validMoves = game.game().validMoves(move.getStartPosition());
        if(!validMoves.contains(move)){
            throw new Exception(" Not a valid Move ");
        }
        if(turn != currentUsersTeam){
            throw new Exception(" Not your turn ");
        }
    }


}
