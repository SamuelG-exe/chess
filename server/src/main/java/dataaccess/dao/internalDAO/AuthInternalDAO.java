package dataaccess.dao.internalDAO;

import dataaccess.DataAccessException;
import dataaccess.dao.AuthDAO;
import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthInternalDAO implements AuthDAO {
    private final Map<String, AuthData> allAuth = new HashMap<>();

    private String token;

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        token = authData.authToken();
        allAuth.put(token, authData);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        AuthData neededAuth = allAuth.get(authToken);
        if(neededAuth==null){
            throw new DataAccessException("Not a Valid authtoken");
        }
        else {
            return neededAuth;
        }
    }

    @Override
    public void deleteAuth(String authToken) {
        allAuth.remove(authToken);
    }

    @Override
    public void clear() {
        allAuth.clear();
    }
}
