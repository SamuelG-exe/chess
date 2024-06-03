package dataaccess.dao.sqlDao;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.dao.GameDAO;
import model.AuthData;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static dataaccess.dao.sqlDao.SQLUtills.executeUpdate;

public class GameSQL implements GameDAO {

    int size = 0;

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        var statement = "INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, chessGame) VALUES (?, ?)";
        var jsonOfGame = new Gson().toJson(gameData.game());
        SQLUtills.executeUpdate(statement, gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), jsonOfGame);
        size++;
    }

    @Override
    public GameData getGame(String gameID) throws DataAccessException {
        var statement = "SELECT authtoken, username FROM auth WHERE authtoken = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(statement)) {
            stmt.setString(1, gameID);
            var sqlLine = stmt.executeQuery();
            if (sqlLine.next()) {
                var gameIDSQL = sqlLine.getString(1);
                var whiteUsername = sqlLine.getString(2);
                var blackUsername = sqlLine.getString(3);
                var gameName = sqlLine.getString(4);
                var chessGameString = sqlLine.getString(5);
                ChessGame chessgame = new Gson().fromJson(chessGameString, ChessGame.class);
                return new GameData(gameIDSQL, whiteUsername, blackUsername, gameName, chessgame);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

        @Override
    public List<GameData> listGames() {
        return null;
    }

    @Override
    public void updateGame(String gameID, GameData gameData) {

    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE games";
        executeUpdate(statement);
        size=0;

    }

    @Override
    public int size() {
        return size;
    }
}
