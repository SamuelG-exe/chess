package dataaccess.dao;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

public interface AuthDAO {

    public void createAuth(AuthData authData) throws DataAccessException;

    public AuthData getAuth(String authToken) throws DataAccessException;

    public void deleteAuth(String authToken);

    public void clear();

    public int size();
}
