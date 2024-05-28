package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.dao.AuthDAO;
import dataaccess.dao.GameDAO;
import dataaccess.dao.internaldao.AuthInternalDAO;
import dataaccess.dao.internaldao.GameInternalDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ListGamesServiceTest {

    private AuthDAO auths;
    private GameDAO games;
    private ListGamesService gameLister;


    @BeforeEach
    void setup() {
        auths = new AuthInternalDAO();
        games = new GameInternalDAO();
    }

    @Test
    void testListGames() throws DataAccessException {
        String username = "SirChessGamer";
        String authToken = "validToken";
        String gameID = "game123";
        ChessGame newGame = new ChessGame();

        auths.createAuth(new AuthData(authToken, username));
        GameData gameTest = new GameData(gameID, null, null, "The one true Game", newGame);
        games.createGame(gameTest);
        gameLister = new ListGamesService();

        gameLister.listGamesService(games, auths, authToken);

    }

    @Test
    void testInvalidAuthtoken() throws DataAccessException {
        String authToken = "NotAValidTokken";
        gameLister = new ListGamesService();
        assertThrows(DataAccessException.class, () -> {
            gameLister.listGamesService(games, auths, authToken);
        });
    }
}
