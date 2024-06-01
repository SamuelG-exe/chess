package dataaccess.dao.sqlDao;

import dataaccess.DataAccessException;
import dataaccess.dao.AuthDAO;
import model.AuthData;
import java.sql.*;

public class AuthSQL implements AuthDAO {
    @Override
    public void createAuth(AuthData authData) throws DataAccessException {

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public void clear() {

    }

    @Override
    public int size() {
        return 0;
    }
}
