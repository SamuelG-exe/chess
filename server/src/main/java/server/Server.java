package server;

import dataaccess.dao.internalDAO.AuthInternalDAO;
import dataaccess.dao.internalDAO.GameInternalDAO;
import dataaccess.dao.internalDAO.UserInternalDAO;
import handler.*;
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

