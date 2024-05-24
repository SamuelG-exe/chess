package server;

import dataaccess.dao.internalDAO.AuthInternalDAO;
import dataaccess.dao.internalDAO.GameInternalDAO;
import dataaccess.dao.internalDAO.UserInternalDAO;
import handler.ClearHandler;
import spark.*;
import dataaccess.dao.*;

public class Server {

    private UserDAO users = new UserInternalDAO();
    private AuthDAO authTokens = new AuthInternalDAO();
    private GameDAO games = new GameInternalDAO();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", (request, response) -> new ClearHandler(users, authTokens, games));
        Spark.post("/user", (request, response) -> new ClearHandler(users, authTokens, games));

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

