package service;

import dataaccess.DataAccessException;
import dataaccess.dao.AuthDAO;
import dataaccess.dao.UserDAO;
import dataaccess.dao.sqldao.AuthSQL;
import dataaccess.dao.sqldao.UserSQL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegisterReq;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RegisterServiceTest {
    private AuthDAO auths;
    private UserDAO users;
    private RegisterService makeNewUser;

    @BeforeEach
    void setup() throws DataAccessException {
        auths = new AuthSQL();
        users = new UserSQL();
        auths.clear();
        users.clear();
        makeNewUser = new RegisterService();
    }

    @Test
    void testValidNewUser() throws DataAccessException {
        String authToken = "1234";
        String username = "SirChessGamer";
        String passWord = "This is the right passWord";
        String email = "user@ChessGamer.com";

        RegisterReq request = new RegisterReq(username, passWord, email);

        assertEquals(0, (auths.size()));
        assertEquals(0, (users.size()));

        makeNewUser.register(request, users, auths);

        assertEquals(1, (auths.size()));
        assertEquals(1, (users.size()));
    }

    @Test
    void testUsernameTaken() throws DataAccessException {
        ///existing user
        String authToken = "1234";
        String username = "SirChessGamer";
        String passWord = "This is the right passWord";
        String email = "user@ChessGamer.com";

        //newUser
        String newUserUsername = "SirChessGamer";
        String newUserPasswor = "passWord";
        String newUserEmail = "newUser@ChessGamer.com";


        RegisterReq existingUser = new RegisterReq(username, passWord, email);
        RegisterReq newUserReq = new RegisterReq(newUserUsername, newUserPasswor, newUserEmail);


        assertEquals(0, (auths.size()));
        assertEquals(0, (users.size()));

        makeNewUser.register(existingUser, users, auths);

        assertThrows(DataAccessException.class, () -> {
            makeNewUser.register(newUserReq, users, auths);

        });

    }


}
