package dataaccess.dao.sqlDao;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.dao.AuthDAO;
import model.AuthData;
import java.sql.*;

import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class AuthSQL implements AuthDAO {
    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO auth (token, name) VALUES (?, ?)";
        Connection connection = null;
        try (Connection c = DatabaseManager.getConnection()) {
            connection = c;
        } catch (SQLException ex) {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.rollback();
                }
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }

        }
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


    private void preformSQLUpdate(String statment){



    }
}
