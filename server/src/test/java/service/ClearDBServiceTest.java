package service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.dao.AuthDAO;
import dataaccess.dao.GameDAO;
import dataaccess.dao.UserDAO;
import dataaccess.dao.internaldao.AuthInternalDAO;
import dataaccess.dao.internaldao.GameInternalDAO;
import dataaccess.dao.internaldao.UserInternalDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;

public class ClearDBServiceTest {

    @Test
    void testClearDB() throws DataAccessException {

        UserDAO users = new UserInternalDAO();
        GameDAO games = new GameInternalDAO();
        AuthDAO tokens = new AuthInternalDAO();
        ChessGame newgame = new ChessGame();

        users.createUser(new UserData("testUser", "password", "taco@tuesdays.com"));
        games.createGame(new GameData("1", "bob", "billyTheKid", "GamersBeGamin", newgame));
        tokens.createAuth(new AuthData("123456", "bob"));

        ClearDBService clearDBService = new ClearDBService();

        // When
        clearDBService.clearDB(users, tokens, games);

        assertEquals(0, users.size());
        assertEquals(0, tokens.size());
        assertEquals(0, games.size());


    }
}
