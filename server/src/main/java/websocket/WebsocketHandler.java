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

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebsocketHandler {

    private final GameManager gameManager = new GameManager();
    private UserDAO usersDAO;
    private AuthDAO authTokensDAO;
    private GameDAO gameDAO;

    public WebsocketHandler(UserDAO users, AuthDAO authTokens, GameDAO games) {
        this.usersDAO = users;
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
                case MAKE_MOVE -> makeMove(SerializeUtils.fromJson(jSONFromClient, MakeMoveCommand.class));
                case LEAVE -> leaveGame(command);
                case RESIGN -> resign(command);
            }
        } catch (UnauthorizedException ex) {
            // Serializes and sends the error message
            sendMessage(session.getRemote(), new ErrorMessage("Error: unauthorized"));
        } catch (Exception ex) {
            ex.printStackTrace();
            sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage()));
        }


    }
    private void connect(Session session,UserGameCommand command) throws DataAccessException, IOException {

        String token = command.getAuthString();
        String gameID = command.getGameID();

        AuthData currntUser= authTokensDAO.getAuth(token);

        gameManager.add(gameID, token, session);
        var message = String.format("%s has joined the Game", currntUser.username());
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        gameManager.broadcast(gameID, token, notification);

    }

    private void leaveGame(UserGameCommand command) throws DataAccessException, IOException {
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
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        gameManager.broadcast(gameID, token, notification);

    }

    private void makeMove(MakeMoveCommand command) throws Exception {
        ChessMove move = command.getMove();
        String token = command.getAuthString();
        String gameID = command.getGameID();


        AuthData currntUser= authTokensDAO.getAuth(token);
        GameData currntGame = gameDAO.getGame(gameID);

        okToMoveChecker(currntGame, currntUser, move);

        var message = String.format("%s has made a Move", currntUser.username());
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        gameManager.broadcast(gameID, token, notification);

    }

    private void resign(UserGameCommand command) throws DataAccessException, IOException {
        String token = command.getAuthString();
        String gameID = command.getGameID();

        AuthData currntUser= authTokensDAO.getAuth(token);
        GameData currntGame = gameDAO.getGame(gameID);

        currntGame.game().setGameOver(true);

        gameDAO.updateGame(gameID, currntGame);

        var message = String.format("%s has resigned", currntUser.username());
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
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
