package dataaccess.dao.sqlDao;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.dao.UserDAO;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static dataaccess.dao.sqlDao.SQLUtills.executeUpdate;

public class UserSQL implements UserDAO {
    int size = 0;
    @Override
    public void createUser(UserData userInfo) throws DataAccessException {

        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
          SQLUtills.executeUpdate(statement, userInfo.username(), userInfo.password(), userInfo.email());
        size++;
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        var statement = "SELECT username, password, email FROM users WHERE username = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(statement)){
            stmt.setString(1, username);
            var sqlLine = stmt.executeQuery();
            if (sqlLine.next()) {
                var userName = sqlLine.getString(1);
                var passwordOfUser = sqlLine.getString(2);
                var emailOfUser = sqlLine.getString(3);
                return new UserData(userName, passwordOfUser, emailOfUser);
            }

        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "DELETE FROM users";
        executeUpdate(statement);
        size=0;

    }

    @Override
    public int size() {
        return size;
    }
}
