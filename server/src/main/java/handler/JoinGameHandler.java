package handler;

import json.SerializeUtils;
import dataaccess.dao.AuthDAO;
import dataaccess.dao.GameDAO;
import dataaccess.dao.UserDAO;
import request.JoinGameReq;
import response.ErrorMessagesResp;
import service.JoinGameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinGameHandler implements Route {
    private UserDAO usersDAO;
    private AuthDAO authTokensDAO;
    private GameDAO gameDAO;
    JoinGameService playerJoin = new JoinGameService();


    public JoinGameHandler(UserDAO users, AuthDAO authTokens, GameDAO games) {
        this.usersDAO = users;
        this.authTokensDAO = authTokens;
        this.gameDAO = games;
    }
    @Override
    public Object handle(Request request, Response response) throws Exception {
        try {
            String token = request.headers("authorization");
            JoinGameReq newReq = SerializeUtils.fromJson(request.body(), JoinGameReq.class);
            playerJoin.join(gameDAO, authTokensDAO, token, newReq);
            response.status(200);
            return "{}";
        }
        catch(Exception e) {
            if(e.getMessage().equals("bad request")) {
                response.status(400);
            }
            else if (e.getMessage().equals("unauthorized")) {
                response.status(401);
            }
            else if (e.getMessage().equals("already taken")) {
                response.status(403);
            }
            else {
                response.status(500);
            }
            return SerializeUtils.toJson(new ErrorMessagesResp("Error: " + e.getMessage()));

        }
    }
}
