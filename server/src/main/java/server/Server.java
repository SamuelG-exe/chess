package server;


import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.dao.sqldao.AuthSQL;
import dataaccess.dao.sqldao.GameSQL;
import dataaccess.dao.sqldao.UserSQL;
import handler.*;
import spark.*;
import dataaccess.dao.*;
import websocket.WebsocketHandler;

public class Server {

    private UserDAO users = new UserSQL();
    private AuthDAO authTokens = new AuthSQL();
    private GameDAO games = new GameSQL();


    public int run(int desiredPort) {
        try {
            DatabaseManager.createDatabase();
            }catch (DataAccessException e) {
                System.err.println("Error creating database: " + e.getMessage());
        }



        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/ws", new WebsocketHandler(users, authTokens, games));
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

