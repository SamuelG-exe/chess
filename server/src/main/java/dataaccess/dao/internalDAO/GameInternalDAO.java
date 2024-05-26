package dataaccess.dao.internalDAO;

import dataaccess.DataAccessException;
import dataaccess.dao.GameDAO;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameInternalDAO implements GameDAO {
    private final Map<String, GameData> allGames = new HashMap<>();

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        if(allGames.containsKey(gameData.gameID())){
            throw new DataAccessException("already taken");
        }
        allGames.put(gameData.gameID(), gameData);
    }

    @Override
    public GameData getGame(String gameID) throws DataAccessException {
        GameData neededGame = allGames.get(gameID);
        if(neededGame == null){
            throw new DataAccessException("No game with that ID Found");
        }
        return neededGame;
    }

    @Override
    public List<GameData> listGames() {
        List<GameData> listOfGames = new ArrayList<>();
        allGames.forEach((integer, gameData) -> listOfGames.add(gameData));
        return listOfGames;
    }

    @Override
    public void updateGame(GameData gameData) {
        allGames.replace(gameData.gameID(), gameData);
    }

    @Override
    public void clear() {
        allGames.clear();
    }
}
