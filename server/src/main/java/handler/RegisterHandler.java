package handler;

import Json.SerializeUtils;
import dataaccess.dao.AuthDAO;
import dataaccess.dao.GameDAO;
import dataaccess.dao.UserDAO;
import service.ClearDBService;
import spark.Request;
import spark.Response;
import spark.Route;

public class RegisterHandler implements Route {
    private UserDAO users;
    private AuthDAO authTokens;

    public RegisterHandler(UserDAO users, AuthDAO authTokens) {
        this.users = users;
        this.authTokens = authTokens;
    }

    ClearDBService cleanSlate = new ClearDBService();
    ErrorMessages errorMessage = new ErrorMessages();

    @Override
    public Object handle(Request request, Response response) throws Exception {
        try {
            //cleanSlate.clearDB(users, authTokens);
            response.status(200);
            return SerializeUtils.toJson(new Object());//might need to replace
        }
        catch (Exception e) {
            response.status(500);
            return SerializeUtils.toJson(errorMessage.message = ("Error: " + e.getMessage()));
        }

    }
}
