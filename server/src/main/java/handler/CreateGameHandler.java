package handler;

import json.SerializeUtils;
import dataaccess.dao.AuthDAO;
import dataaccess.dao.GameDAO;
import response.CreateGameResp;
import response.ErrorMessagesResp;
import service.CreateGameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateGameHandler implements Route {

    private AuthDAO authTokensDAO;
    private GameDAO gameDAO;

    CreateGameService newGame = new CreateGameService();

    public CreateGameHandler(AuthDAO authTokens, GameDAO games) {
        this.gameDAO = games;
        this.authTokensDAO = authTokens;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        try {
            String token = request.headers("authorization");
            String gameName = request.body();
            CreateGameResp newResp = newGame.makeNewGame(gameDAO, authTokensDAO, token, gameName);
            response.status(200);
            return SerializeUtils.toJson(newResp);
        }
        catch (Exception e) {
            if(e.getMessage().equals("bad request")) {
                response.status(400);
            }
            else if (e.getMessage().equals("unauthorized")) {
                response.status(401);
            }
            else {
                response.status(500);
            }
            return SerializeUtils.toJson(new ErrorMessagesResp("Error: " + e.getMessage()));
        }
    }
}
