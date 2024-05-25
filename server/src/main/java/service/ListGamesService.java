package service;

import dataaccess.DataAccessException;
import dataaccess.dao.AuthDAO;
import dataaccess.dao.GameDAO;
import model.GameData;
import response.ListGamesResp;

import java.util.List;

public class ListGamesService {

    ServiceUtils utils = new ServiceUtils();

    public ListGamesResp listGamesService(GameDAO games, AuthDAO authTokens, String authToken) throws DataAccessException {
        utils.verifyAuth(authTokens, authToken);
        List<GameData> listOfGames = games.listGames();
        return new ListGamesResp(listOfGames);
    }
}



