package service;

import dataaccess.DataAccessException;
import dataaccess.dao.AuthDAO;
import dataaccess.dao.UserDAO;
import model.AuthData;
import model.UserData;
import request.LoginReq;
import request.RegisterReq;
import response.LoginResp;
import response.RegisterResp;

import java.util.Objects;

public class LoginService {


    public LoginResp login(LoginReq request, UserDAO users, AuthDAO authTokens) throws DataAccessException {
        UserData returningUser = users.getUser(request.username());
        if(!Objects.equals(returningUser.password(), request.password())){
            throw new DataAccessException("unauthorized");
        }
        String newAuthToken =  new ServiceUtils().makeToken();
        AuthData newToken = new AuthData(newAuthToken, request.username());
        authTokens.createAuth(newToken);
        return new LoginResp(returningUser.username(), newAuthToken);

    }
}
