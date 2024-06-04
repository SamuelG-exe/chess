package dataaccess.dao.sqldao;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.dao.GameDAO;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static dataaccess.dao.sqldao.SQLUtills.executeUpdate;

public class GameSQL implements GameDAO {

    int size = 0;

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        var statement = "INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, chessGame) VALUES (?, ?, ?, ?, ?)";
        var jsonOfGame = new Gson().toJson(gameData.game());
        SQLUtills.executeUpdate(statement, gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), jsonOfGame);
        size++;
    }

    @Override
    public GameData getGame(String gameID) throws DataAccessException {
        var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, chessGame FROM games WHERE gameID = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(statement)) {
            stmt.setInt(1, Integer.parseInt(gameID));
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
    public List<GameData> listGames() throws DataAccessException {
            List<GameData> listOfGames = new ArrayList<>();
            try (var conn = DatabaseManager.getConnection()) {
                var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, chessGame FROM games";
                try (var ps = conn.prepareStatement(statement)) {
                    try (var rs = ps.executeQuery()) {
                        while (rs.next()) {
                            String gameID = rs.getString(1);
                            GameData gameData = getGame(gameID);
                            listOfGames.add(gameData);
                        }
                    }
                }
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
            return listOfGames;
    }


    @Override
    public void updateGame(String gameID, GameData gameData) throws DataAccessException {
        var statement = "UPDATE games SET whiteUsername = ?, blackUsername = ?, chessGame = ? WHERE gameID = ?";
        var jsonOfGame = new Gson().toJson(gameData.game());
        SQLUtills.executeUpdate(statement, gameData.whiteUsername(), gameData.blackUsername(), jsonOfGame, gameID);

    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "DELETE FROM games";
        executeUpdate(statement);
        size=0;

    }

    @Override
    public int size() {
        return size;
    }
}
