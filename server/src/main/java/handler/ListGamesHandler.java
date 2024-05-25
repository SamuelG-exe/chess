package handler;

import Json.SerializeUtils;
import dataaccess.dao.AuthDAO;
import dataaccess.dao.GameDAO;
import dataaccess.dao.UserDAO;
import request.RegisterReq;
import response.ListGamesResp;
import response.RegisterResp;
import service.ListGamesService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ListGamesHandler implements Route {
    private AuthDAO authTokensDAO;
    private GameDAO gameDAO;
    ErrorMessages errorMessage = new ErrorMessages();
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
            if (e.getMessage().equals("unauthorized")) {
                response.status(401);
            }
            else {
                response.status(500);
            }
            return SerializeUtils.toJson(errorMessage.message = ("Error: " + e.getMessage()));
        }


    }
}
