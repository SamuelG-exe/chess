package handler;

import Json.SerializeUtils;
import dataaccess.dao.AuthDAO;
import dataaccess.dao.UserDAO;
import request.LoginReq;
import response.LoginResp;
import service.LoginService;

import spark.Request;
import spark.Response;
import spark.Route;
public class LoginHandler implements Route {


    private UserDAO usersDAO;
    private AuthDAO authTokensDAO;


    public LoginHandler(UserDAO users, AuthDAO authTokens) {
        this.usersDAO = users;
        this.authTokensDAO = authTokens;
    }

    LoginService userLogin = new LoginService();
    ErrorMessages errorMessage = new ErrorMessages();


    @Override
    public Object handle(Request request, Response response) throws Exception {
        try {
            LoginReq newReq = SerializeUtils.fromJson(request.body(), LoginReq.class);
            LoginResp newResp = userLogin.login(newReq, usersDAO, authTokensDAO);
            response.status(200);
            return SerializeUtils.toJson(newResp);//might need to replace
        } catch (Exception e) {
            if (e.getMessage().equals("unauthorized")) {
                response.status(401);
            } else {
                response.status(500);
            }
            return SerializeUtils.toJson(errorMessage.message = ("Error: " + e.getMessage()));
        }
    }
}