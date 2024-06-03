package dataaccess.dao;

import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface GameDAO {

    public void createGame(GameData gameData) throws DataAccessException;

    public GameData getGame(String gameID) throws DataAccessException;

    public List<GameData> listGames() throws DataAccessException;

    public void updateGame(String gameID, GameData gameData) throws DataAccessException;

    public void clear() throws DataAccessException;

    public int size();

}
