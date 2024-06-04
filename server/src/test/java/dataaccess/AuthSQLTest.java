package dataaccess;

import dataaccess.dao.sqldao.AuthSQL;
import dataaccess.dao.sqldao.UserSQL;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ServiceUtils;

import static org.junit.jupiter.api.Assertions.*;

public class AuthSQLTest {

    UserSQL users = new UserSQL();
    AuthSQL tokens = new AuthSQL();

    ServiceUtils tokenMaker = new ServiceUtils();

    @BeforeEach
    void setup() throws DataAccessException {
        users = new UserSQL();
        tokens = new AuthSQL();
        tokens.clear();
        users.clear();
    }
    @AfterEach
    void cleanUp() throws DataAccessException {
        tokens.clear();
        users.clear();
    }

    @Test
    void testCreateAuthPos() throws DataAccessException {

        assertEquals(0, users.size());
        assertEquals(0, tokens.size());
        users.createUser(new UserData("testUser", "password", "taco@tuesdays.com"));
        tokens.createAuth(new AuthData(tokenMaker.makeToken(), "testUser"));
        assertEquals(1, users.size());
        assertEquals(1, tokens.size());
    }

    @Test
    void testCreateAuthNeg() throws DataAccessException {
        users.createUser(new UserData("testUser", "password", "taco@tuesdays.com"));
        var thisToken = tokenMaker.makeToken();
        tokens.createAuth(new AuthData(thisToken, "testUser"));
        tokens.createAuth(new AuthData(tokenMaker.makeToken(), "testUser"));
        assertEquals(2, tokens.size());
    }

    @Test
    void testGetAuthPos() throws DataAccessException {
        users.createUser(new UserData("testUser", "password", "taco@tuesdays.com"));
        var thisToken = tokenMaker.makeToken();
        var thatToken = tokenMaker.makeToken();
        tokens.createAuth(new AuthData(thisToken, "testUser"));
        tokens.createAuth(new AuthData(thatToken, "testUser"));
        assertEquals("testUser", tokens.getAuth(thisToken).username());
        assertEquals("testUser", tokens.getAuth(thatToken).username());

    }

    @Test
    void testGetAuthNeg() throws DataAccessException {
        users.createUser(new UserData("testUser", "password", "taco@tuesdays.com"));
        var thisToken = tokenMaker.makeToken();
        tokens.createAuth(new AuthData(thisToken, "testUser"));

        assertThrows(DataAccessException.class, () -> {
            tokens.getAuth(tokenMaker.makeToken());
        });

    }

    @Test
    void testDeleteAuthPos() throws DataAccessException {
        users.createUser(new UserData("testUser", "password", "taco@tuesdays.com"));
        var thisToken = tokenMaker.makeToken();
        tokens.createAuth(new AuthData(thisToken, "testUser"));

        assertEquals(1, users.size());
        assertEquals(1, tokens.size());

        tokens.deleteAuth(thisToken);

        assertEquals(0, tokens.size());
    }

    @Test
    void testDeleteAuthNeg() throws DataAccessException {
        users.createUser(new UserData("testUser", "password", "taco@tuesdays.com"));
        users.createUser(new UserData("testUser2", "password", "bacon@tuesdays.com"));

        var thisToken = tokenMaker.makeToken();
        var thatToken = tokenMaker.makeToken();
        tokens.createAuth(new AuthData(thisToken, "testUser"));
        tokens.createAuth(new AuthData(thatToken, "testUser2"));

        assertEquals(2, tokens.size());

        tokens.deleteAuth(thatToken);

        assertEquals(1, tokens.size());
        assertEquals("testUser", tokens.getAuth(thisToken).username());
    }

    @Test
    void testClear() throws DataAccessException {
        assertEquals(0, tokens.size());

        users.createUser(new UserData("testUser", "password", "taco@tuesdays.com"));
        var thisToken = tokenMaker.makeToken();
        tokens.createAuth(new AuthData(thisToken, "testUser"));

        assertEquals(1, tokens.size());
        tokens.clear();
        assertEquals(0, tokens.size());

    }

}
