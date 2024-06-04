package dataaccess;

import chess.ChessGame;
import dataaccess.dao.sqldao.GameSQL;
import dataaccess.dao.sqldao.UserSQL;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class GameSQLTest {

    UserSQL users = new UserSQL();
    GameSQL games = new GameSQL();

    @BeforeEach
    void setup() throws DataAccessException {
        users = new UserSQL();
        games = new GameSQL();
        games.clear();
        users.clear();
    }

    @AfterEach
    void cleanUp() throws DataAccessException {
        games.clear();
        users.clear();
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

    @Test
    void testUpdateGamePos() throws DataAccessException {
        users.createUser(new UserData("whiteJoinTest", "password", "taco@tuesdays.com"));
        ChessGame game = new ChessGame();
        games.createGame(new GameData("7", null, null, "nameOfGame", game));

        GameData gameUpdate = new GameData("7", "whiteJoinTest", null, "nameOfGame", game);
        games.updateGame("7", gameUpdate);

        assertEquals("whiteJoinTest", games.getGame("7").whiteUsername());
    }

    @Test
    void testUpdateGameNeg() throws DataAccessException {
        users.createUser(new UserData("whiteJoinTest", "password", "taco@tuesdays.com"));
        ChessGame game = new ChessGame();
        games.createGame(new GameData("7", null, null, "nameOfGame", game));

        GameData gameUpdate = new GameData("7", "whiteJoinTest", null, "nameOfGame", game);
        games.updateGame("5", gameUpdate);

        assertNotEquals("whiteJoinTest", games.getGame("7").whiteUsername());

    }
    @Test
    void testClear() throws DataAccessException {

        assertEquals(0, games.size());
        games.createGame(new GameData("7", null, null, "nameOfGame", new ChessGame()));
        assertEquals(1, games.size());
        games.clear();
        assertEquals(0, games.size());

    }
}
