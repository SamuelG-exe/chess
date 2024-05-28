package service;

import dataaccess.DataAccessException;
import dataaccess.dao.AuthDAO;
import dataaccess.dao.UserDAO;
import dataaccess.dao.internaldao.AuthInternalDAO;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LogoutServiceTest {
    private AuthDAO auths;
    private UserDAO users;
    private LogoutService logOut;

    @BeforeEach
    void setup() {
        auths = new AuthInternalDAO();
        logOut = new LogoutService();
    }

    @Test
    void testLogOutCorrect() throws DataAccessException {
        String authToken = "1234";
        String username = "SirChessGamer";
        auths.createAuth(new AuthData(authToken, username));

        assertEquals(1, (auths.size()));

        logOut.logout(auths, authToken);

        assertEquals(0, (auths.size()));
    }

    @Test
    void testLogOutInvalidAuth() throws DataAccessException {
        String authToken = "1234";
        String falseToken = "incorrect";
        String username = "SirChessGamer";
        auths.createAuth(new AuthData(authToken, username));

        assertEquals(1, (auths.size()));

        assertThrows(DataAccessException.class, () -> {
            logOut.logout(auths, falseToken);
        });

    }
}
