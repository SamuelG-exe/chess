package service;

import dataaccess.DataAccessException;
import dataaccess.dao.AuthDAO;
import dataaccess.dao.UserDAO;
import dataaccess.dao.internalDAO.AuthInternalDAO;
import dataaccess.dao.internalDAO.GameInternalDAO;
import dataaccess.dao.internalDAO.UserInternalDAO;
import handler.LoginHandler;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginReq;
import response.LoginResp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LoginServiceTest {
    private AuthDAO auths;
    private UserDAO users;
    private LoginService login;

    @BeforeEach
    void setup() {
        auths = new AuthInternalDAO();
        users = new UserInternalDAO();
        login = new LoginService();
    }

    @Test
    void testLoginCorrect() throws DataAccessException {
        String authToken = "1234";
        String username = "SirChessGamer";
        String passWord = "This is the right passWord";
        String email = "user@ChessGamer.com";
        UserData newUser = new UserData(username,passWord, email);

        auths.createAuth(new AuthData(authToken, username));
        users.createUser(newUser);

        LoginReq request = new LoginReq(username, passWord);

        LoginResp response = login.login(request, users, auths);

        assertEquals(1, ( users.size()));
        UserData loggedIn = (users.getUser(response.username()));
        assertEquals("SirChessGamer", loggedIn.username());
        assertEquals("This is the right passWord", loggedIn.password());

    }

    @Test
    void testLoginWithWrongPassWord() throws DataAccessException {
        String authToken = "1234";
        String username = "SirChessGamer";
        String passWord = "This is the right passWord";
        String email = "user@ChessGamer.com";
        UserData newUser = new UserData(username,passWord, email);

        auths.createAuth(new AuthData(authToken, username));
        users.createUser(newUser);

        LoginReq request = new LoginReq(username, "incorrect passWord");

        assertThrows(DataAccessException.class, () -> {
            login.login(request, users, auths);
        });
    }

}
