package dataaccess.dao.sqlDao;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.dao.AuthDAO;
import model.AuthData;
import java.sql.*;

import static dataaccess.dao.sqlDao.SQLUtills.executeUpdate;

public class AuthSQL implements AuthDAO {
    int size = 0;

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO auth (authtoken, username) VALUES (?, ?)";
        try(Connection connection = DatabaseManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(statement)) {
            stmt.setString(1, authData.authToken());
            stmt.setString(2, authData.username());
            stmt.executeUpdate();
            size++;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }


    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        var statement = "SELECT authtoken, username FROM auth WHERE authtoken = ?";
        try(Connection connection = DatabaseManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(statement)) {
            stmt.setString(1, authToken);
            var sqlLine = stmt.executeQuery();
            if (sqlLine.next()) {
                var token = sqlLine.getString(1);
                var user = sqlLine.getString(2);
                if (token == null || user == null) {
                    throw new DataAccessException("unauthorized");
                }
                return new AuthData(token, user);
            }
            else {
                throw new DataAccessException("unauthorized");
            }

        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE authToken = ?";
        executeUpdate(statement, authToken);
        size--;

    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "DELETE FROM auth";
        executeUpdate(statement);
        size=0;

    }

    @Override
    public int size() {
        return size;
    }



}
