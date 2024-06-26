package dataaccess;

import dataaccess.dao.sqldao.AuthSQL;
import dataaccess.dao.sqldao.GameSQL;
import dataaccess.dao.sqldao.UserSQL;
import model.UserData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserSQLTest {
    UserSQL users = new UserSQL();
    AuthSQL tokens = new AuthSQL();
    GameSQL games = new GameSQL();


    @BeforeEach
    void setup() throws DataAccessException {
        tokens.clear();
        games.clear();
        users.clear();
    }

    @AfterEach
    void cleanUp() throws DataAccessException {
        users.clear();
    }



    @Test
    void testClearDB() throws DataAccessException {
        assertEquals(0, users.size());
        users.createUser(new UserData("testUser", "password", "taco@tuesdays.com"));
        assertEquals(1, users.size());
        users.clear();
        assertEquals(0, users.size());
    }

    @Test
    void testCreateUserPos() throws DataAccessException {
        assertEquals(0, users.size());
        users.createUser(new UserData("testUser", "password", "taco@tuesdays.com"));
        assertEquals(1, users.size());
    }

    @Test
    void testCreateUserNeg() throws DataAccessException {
        assertEquals(0, users.size());
        users.createUser(new UserData("testUser", "password", "taco@tuesdays.com"));
        assertEquals(1, users.size());
        assertThrows(DataAccessException.class, () -> {
            users.createUser(new UserData("testUser", "newpassword", "New@email.com"));

        });
    }

    @Test
    void testGetUserPos() throws DataAccessException {
        assertEquals(0, users.size());
        users.createUser(new UserData("testUser", "password", "taco@tuesdays.com"));



        assertEquals("testUser", users.getUser("testUser").username());
        assertEquals("password", users.getUser("testUser").password());
    }

    @Test
    void testGetUserNullandNeg() throws DataAccessException {
        assertEquals(0, users.size());
        users.createUser(new UserData("testUser", "password", "taco@tuesdays.com"));

        assertNull(users.getUser(null));
    }


}
