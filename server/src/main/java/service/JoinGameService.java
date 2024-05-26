package service;

import dataaccess.DataAccessException;
import dataaccess.dao.AuthDAO;
import dataaccess.dao.GameDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import request.JoinGameReq;

public class JoinGameService {
    ServiceUtils utils = new ServiceUtils();
    GameData gameToBeJoined;

    public void join(GameDAO games, AuthDAO authTokens, String authToken, JoinGameReq request) throws DataAccessException {
        utils.verifyAuth(authTokens, authToken);
        games.getGame(request.gameID());
        gameToBeJoined = games.getGame(request.gameID());
        if (request.gameID() == null || request.playerColor()==null){
            assert request.playerColor() != null;
            if(!(request.playerColor().equals("WHITE") || request.playerColor().equals("BLACK"))) {
                throw new DataAccessException("bad request");
            }
        }

        AuthData userJoingAuth = authTokens.getAuth(authToken);
        String newPlayer = userJoingAuth.username();

        GameData updatedGame;
        if(request.playerColor().equals("WHITE")){
            if (gameToBeJoined.whiteUsername() != null){
                throw new DataAccessException("bad request");
            }
            updatedGame = new GameData(gameToBeJoined.gameID(), newPlayer, gameToBeJoined.blackUsername(), gameToBeJoined.gameName(), gameToBeJoined.game());
        }
        else{
            if (gameToBeJoined.blackUsername() != null){
                throw new DataAccessException("bad request");
            }
            updatedGame = new GameData(gameToBeJoined.gameID(), gameToBeJoined.whiteUsername(), newPlayer, gameToBeJoined.gameName(), gameToBeJoined.game());
        }
        games.updateGame(updatedGame);

    }
}
