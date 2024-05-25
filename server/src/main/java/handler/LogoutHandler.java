package handler;

import Json.SerializeUtils;
import dataaccess.dao.AuthDAO;
import service.LogoutService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route {
    private AuthDAO authTokensDAO;
    ErrorMessages errorMessage = new ErrorMessages();


    public LogoutHandler(AuthDAO authTokens) {
        this.authTokensDAO = authTokens;
    }

    LogoutService userLogout = new LogoutService();

    @Override
    public Object handle(Request request, Response response) throws Exception {
        try {
            String token = request.headers("authorization");
            userLogout.logout(authTokensDAO, token);
            response.status(200);
            return SerializeUtils.toJson("{}");//might need to replace
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
