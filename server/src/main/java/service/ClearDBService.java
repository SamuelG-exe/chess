package service;
import dataaccess.dao.*;

public class ClearDBService {
    private AuthDAO authDAO;

    public void clearDB(UserDAO users, AuthDAO authTokens, GameDAO games) {
        users.clear();
        authTokens.clear();
        games.clear();

    }
}
