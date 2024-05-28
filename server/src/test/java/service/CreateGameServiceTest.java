package service;

import dataaccess.DataAccessException;
import dataaccess.dao.AuthDAO;
import dataaccess.dao.GameDAO;
import dataaccess.dao.internaldao.AuthInternalDAO;
import dataaccess.dao.internaldao.GameInternalDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import response.CreateGameResp;

import static org.junit.jupiter.api.Assertions.*;

public class CreateGameServiceTest {
    private AuthDAO auths;
    private GameDAO games;
    private CreateGameService makeGame;

    @BeforeEach
    void testSeUps() {
        auths = new AuthInternalDAO();
        games = new GameInternalDAO();
        makeGame = new CreateGameService();
    }

    @Test
    void testMakeNewGame() throws DataAccessException {
        String authToken = "1234";
        String username = "SirChessGamer";
        String gameName = "Test Game";

        auths.createAuth(new AuthData(authToken, username));

        CreateGameResp response = makeGame.makeNewGame(games, auths, authToken, gameName);

        assertNotNull(response.gameID());
        assertEquals(1, ( games.size()));
        GameData createdGame = (games.getGame(response.gameID()));
        assertEquals("Test Game", createdGame.gameName());
    }

    @Test
    void testNullGameName() throws DataAccessException {
        String authToken = "Super cool token";
        String username = "SirChessGamer";
        String gameName = null;

        auths.createAuth(new AuthData(authToken, username));

        // When & Then
        assertThrows(DataAccessException.class, () -> {
            makeGame.makeNewGame(games, auths, authToken, gameName);
        });
    }
}