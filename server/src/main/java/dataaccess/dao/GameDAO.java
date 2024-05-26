package dataaccess.dao;

import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;

import java.util.List;
import java.util.Map;

public interface GameDAO {

    public void createGame(GameData gameData) throws DataAccessException;

    public GameData getGame(String gameID) throws DataAccessException;

    public List<GameData> listGames();

    public void updateGame(String gameID, GameData gameData);

    public void clear();

}
