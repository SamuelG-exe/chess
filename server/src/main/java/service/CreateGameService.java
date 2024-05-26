package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.dao.AuthDAO;
import dataaccess.dao.GameDAO;
import model.GameData;
import response.CreateGameResp;

public class CreateGameService {
    ServiceUtils utils = new ServiceUtils();
    GameData newGame;

    public CreateGameResp makeNewGame(GameDAO games, AuthDAO authTokens, String authToken, String gameName) throws DataAccessException {
        utils.verifyAuth(authTokens, authToken);
        if (gameName == null){
            throw new DataAccessException("bad request");
        }
        String gameID = utils.newID();
        newGame = new GameData(gameID, null, null, gameName, new ChessGame());
        games.createGame(newGame);

        return new CreateGameResp(gameID);
    }
}
