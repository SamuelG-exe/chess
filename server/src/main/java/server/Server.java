package server;


import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.dao.internaldao.AuthInternalDAO;
import dataaccess.dao.internaldao.GameInternalDAO;
import dataaccess.dao.internaldao.UserInternalDAO;
import dataaccess.dao.sqlDao.AuthSQL;
import handler.*;
import spark.*;
import dataaccess.dao.*;

import java.sql.Connection;
import java.sql.SQLException;

public class Server {

    private UserDAO users = new UserInternalDAO();
    private AuthDAO authTokens = new AuthSQL();
    private GameDAO games = new GameInternalDAO();

    public int run(int desiredPort) {
        try {
            DatabaseManager.createDatabase();
            }catch (DataAccessException e) {
                System.err.println("Error creating database: " + e.getMessage());
        }



        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", (request, response) -> new ClearHandler(users, authTokens, games).handle(request, response));
        Spark.post("/user", (request, response) -> new RegisterHandler(users, authTokens).handle(request, response));
        Spark.post("/session", (request, response) -> new LoginHandler(users, authTokens).handle(request, response));
        Spark.delete("/session", (request, response) -> new LogoutHandler(authTokens).handle(request, response));
        Spark.get("/game", (request, response) -> new ListGamesHandler(authTokens, games).handle(request, response));
        Spark.post("/game", (request, response) -> new CreateGameHandler(authTokens, games).handle(request, response));
        Spark.put("/game", (request, response) -> new JoinGameHandler(users, authTokens, games).handle(request, response));


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
    public static void main(String[] args) {
        new Server().run(8080);
    }

}

