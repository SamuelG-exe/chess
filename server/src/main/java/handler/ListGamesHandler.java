package handler;

import json.SerializeUtils;
import dataaccess.dao.AuthDAO;
import dataaccess.dao.GameDAO;
import response.ErrorMessagesResp;
import response.ListGamesResp;
import service.ListGamesService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ListGamesHandler implements Route {
    private AuthDAO authTokensDAO;
    private GameDAO gameDAO;
    ListGamesService gameList = new ListGamesService();

    public ListGamesHandler(AuthDAO authTokens, GameDAO games) {
        this.gameDAO = games;
        this.authTokensDAO = authTokens;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        try {
            String token = request.headers("authorization");
            ListGamesResp newResp = gameList.listGamesService(gameDAO, authTokensDAO, token);
            response.status(200);
            return SerializeUtils.toJson(newResp);
        }
        catch (Exception e) {
            if (!(e.getMessage().equals("unauthorized"))) {
                response.status(500);
            }
            else {
                response.status(401);
            }
            return SerializeUtils.toJson(new ErrorMessagesResp("Error: " + e.getMessage()));
        }


    }
}
