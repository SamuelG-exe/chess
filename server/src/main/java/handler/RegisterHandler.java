package handler;

import json.SerializeUtils;
import dataaccess.dao.AuthDAO;
import dataaccess.dao.UserDAO;
import request.RegisterReq;
import response.ErrorMessagesResp;
import response.RegisterResp;
import service.RegisterService;
import spark.Request;
import spark.Response;
import spark.Route;

public class RegisterHandler implements Route {
    private UserDAO usersDAO;
    private AuthDAO authTokensDAO;


    public RegisterHandler(UserDAO users, AuthDAO authTokens) {
        this.usersDAO = users;
        this.authTokensDAO = authTokens;
    }

    RegisterService newUser = new RegisterService();

    @Override
    public Object handle(Request request, Response response) throws Exception {
        try {
            RegisterReq newReq = SerializeUtils.fromJson(request.body(), RegisterReq.class);
            RegisterResp newResp = newUser.register(newReq, usersDAO, authTokensDAO);
            response.status(200);
            return SerializeUtils.toJson(newResp);//might need to replace
        }
        catch (Exception e) {
            if(e.getMessage().equals("bad request")){
                response.status(400);
            }
            else if(e.getMessage().equals("already taken")){
                response.status(403);
            }
            else{
                response.status(500);
            }
            return SerializeUtils.toJson(new ErrorMessagesResp("Error: " + e.getMessage()));
        }

    }
}
