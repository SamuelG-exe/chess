package service;

import dataaccess.DataAccessException;
import dataaccess.dao.AuthDAO;
import dataaccess.dao.UserDAO;
import request.RegisterReq;
import response.RegisterResp;
import model.*;

public class RegisterService {

    ServiceUtils utils = new ServiceUtils();

    UserData newUser;
    AuthData newAuth;
    public RegisterResp register(RegisterReq request, UserDAO users, AuthDAO authTokens)  throws DataAccessException {
        if(request.username() == null || request.password() == null || request.email() == null){
            throw new DataAccessException("bad request");
        }
        newUser = new UserData(request.username(), request.password(), request.email());
        newAuth = new AuthData(utils.makeToken(), request.username());

        users.createUser(newUser);
        authTokens.createAuth(newAuth);

        return new RegisterResp(newUser.username(), newAuth.authToken());
    }
}
