package handler;

import dataaccess.dao.AuthDAO;
import dataaccess.dao.GameDAO;
import dataaccess.dao.UserDAO;
import dataaccess.dao.internalDAO.AuthInternalDAO;
import dataaccess.dao.internalDAO.GameInternalDAO;
import dataaccess.dao.internalDAO.UserInternalDAO;
import response.ErrorMessagesResp;
import spark.Request;
import spark.Response;
import spark.Route;
import Json.SerializeUtils;
import service.ClearDBService;

public class ClearHandler implements Route {
    private UserDAO users;
    private AuthDAO authTokens;
    private GameDAO games;

    public ClearHandler(UserDAO users, AuthDAO authTokens, GameDAO games){
        this.users = users;
        this.authTokens = authTokens;
        this.games = games;
    }

    ClearDBService cleanSlate = new ClearDBService();

    @Override
    public Object handle(Request request, Response response) throws Exception {
        try {
            cleanSlate.clearDB(users, authTokens, games);
            response.status(200);
            return "{}";//might need to replace
        }
        catch (Exception e) {
            response.status(500);
            return SerializeUtils.toJson(new ErrorMessagesResp("Error: " + e.getMessage()));
        }

    }


}
