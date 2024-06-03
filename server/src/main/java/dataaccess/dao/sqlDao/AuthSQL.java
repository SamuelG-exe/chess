package dataaccess.dao.sqlDao;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.dao.AuthDAO;
import model.AuthData;
import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class AuthSQL implements AuthDAO {
    Connection connection = null;

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO auth (token, name) VALUES (?, ?)";
        connection = getConnnectionInAuth();
        try(PreparedStatement stmt = connection.prepareStatement(statement)) {
            stmt.setString(1, authData.authToken());
            stmt.setString(2, authData.username());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        var statement = "SELECT authtoken, username FROM auth WHERE authtoken = ?";
        connection = getConnnectionInAuth();
        try(PreparedStatement stmt = connection.prepareStatement(statement)) {
            stmt.setString(1, authToken);
            var sqlLine = stmt.executeQuery();
            if (sqlLine.next()) {
                var token = sqlLine.getString(1);
                var user = sqlLine.getString(2);
                return new AuthData(token, user);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM pet WHERE id=?";
        connection = getConnnectionInAuth();

    }

    @Override
    public void clear() {

    }

    @Override
    public int size() {
        return 0;
    }


    private Connection getConnnectionInAuth() throws DataAccessException {
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
        return connection;
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof ChessGame p) ps.setString(i + 1, p.toString());
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
