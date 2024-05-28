package service;
import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.dao.AuthDAO;
import dataaccess.dao.GameDAO;
import dataaccess.dao.internalDAO.AuthInternalDAO;
import dataaccess.dao.internalDAO.GameInternalDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.JoinGameReq;
import response.CreateGameResp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JoinGameServiceTest {
    private AuthDAO auths;
    private GameDAO games;
    private JoinGameService gameJoin;

    @BeforeEach
    void setup() {
        auths = new AuthInternalDAO();
        games = new GameInternalDAO();
        gameJoin = new JoinGameService();
    }

    @Test
    void testJoinWhitePlayer() throws DataAccessException {
        String authToken = "1234";
        String username = "SirChessGamer";
        String gameName = "Test Game";
        String gameID = "game123";
        ChessGame newgame = new ChessGame();



        auths.createAuth(new AuthData(authToken, username));
        GameData gameTest = new GameData(gameID, null, null, gameName, newgame);
        games.createGame(gameTest);
        JoinGameReq joinGameTest = new JoinGameReq("WHITE", gameID);

        gameJoin.join(games, auths, authToken, joinGameTest);

        GameData updatedGame = games.getGame(gameID);
        assertEquals("SirChessGamer", updatedGame.whiteUsername());

    }

    @Test
    void testNotAValidColor() throws DataAccessException {
        String authToken = "1234";
        String username = "SirChessGamer";
        String gameName = "Test Game";
        String gameID = "game123";
        ChessGame newgame = new ChessGame();



        auths.createAuth(new AuthData(authToken, username));
        GameData gameTest = new GameData(gameID, null, null, gameName, newgame);
        games.createGame(gameTest);
        JoinGameReq joinGameTest = new JoinGameReq("Taco", gameID);

        assertThrows(DataAccessException.class, () -> {
            gameJoin.join(games, auths, authToken, joinGameTest);
        });

    }
}
