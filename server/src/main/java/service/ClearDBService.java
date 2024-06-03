package service;
import dataaccess.DataAccessException;
import dataaccess.dao.*;

public class ClearDBService {
    private AuthDAO authDAO;

    public void clearDB(UserDAO users, AuthDAO authTokens, GameDAO games) throws DataAccessException {
        users.clear();
        authTokens.clear();
        games.clear();

    }
}
