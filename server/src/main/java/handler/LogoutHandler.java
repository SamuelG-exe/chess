package handler;

import json.SerializeUtils;
import dataaccess.dao.AuthDAO;
import response.ErrorMessagesResp;
import service.LogoutService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route {
    private AuthDAO authTokensDAO;

    LogoutService userLogout = new LogoutService();

    public LogoutHandler(AuthDAO authTokens) {
        this.authTokensDAO = authTokens;
    }


    @Override
    public Object handle(Request request, Response response) throws Exception {
        try {
            String token = request.headers("authorization");
            userLogout.logout(authTokensDAO, token);
            response.status(200);
            return "{}";//might need to replace
        }
        catch (Exception e) {
            if (e.getMessage().equals("unauthorized")) {
                response.status(401);
            }
            else {
                response.status(500);
            }
            return SerializeUtils.toJson(new ErrorMessagesResp("Error: " + e.getMessage()));
        }
    }
}
