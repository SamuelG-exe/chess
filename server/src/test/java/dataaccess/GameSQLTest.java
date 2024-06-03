package dataaccess;

import chess.ChessGame;
import dataaccess.dao.sqlDao.GameSQL;
import dataaccess.dao.sqlDao.UserSQL;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.RegisterService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


public class GameSQLTest {

    UserSQL users = new UserSQL();
    GameSQL games = new GameSQL();

    @BeforeEach
    void setup() {
        users = new UserSQL();
        games = new GameSQL();
    }

    @AfterEach
    void cleanUp() throws DataAccessException {
        users.clear();
        games.clear();
    }

    @Test
    void testCreateGamePos() throws DataAccessException {

        assertEquals(0, users.size());
        assertEquals(0, games.size());
        users.createUser(new UserData("testUser", "password", "taco@tuesdays.com"));
        games.createGame(new GameData("7", null, null, "nameOfGame", new ChessGame()));
        assertEquals(1, users.size());
        assertEquals(1, games.size());
    }

    @Test
    void testCreateGameNeg() throws DataAccessException {
        users.createUser(new UserData("testUser", "password", "taco@tuesdays.com"));
        games.createGame(new GameData("7", null, null, "nameOfGame", new ChessGame()));
        assertThrows(DataAccessException.class, () -> {
            games.createGame(new GameData("7", null, null, "nameOfGame", new ChessGame()));
        });
    }

    @Test
    void testGetGamePos() throws DataAccessException {

        users.createUser(new UserData("testUser", "password", "taco@tuesdays.com"));
        games.createGame(new GameData("7", null, null, "nameOfGame", new ChessGame()));
        assertEquals("nameOfGame", games.getGame("7").gameName());
    }

    @Test
    void testGetGameNeg() throws DataAccessException {

        users.createUser(new UserData("testUser", "password", "taco@tuesdays.com"));
        games.createGame(new GameData("7", null, null, "nameOfGame", new ChessGame()));
        assertThrows(DataAccessException.class, () -> {
            games.createGame(new GameData("ShouldntWork", null, null, "nameOfGame", new ChessGame()));
        });
    }

    @Test
    void testListGamePos() throws DataAccessException {
        games.createGame(new GameData("7", null, null, "nameOfGame", new ChessGame()));
        assertNotNull(games.listGames());

    }

    @Test
    void testListGameNeg() throws DataAccessException {
        assertEquals(0, games.listGames().size());

    }

}
