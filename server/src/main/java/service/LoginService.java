package service;

import dataaccess.DataAccessException;
import dataaccess.dao.AuthDAO;
import dataaccess.dao.UserDAO;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import request.LoginReq;
import request.RegisterReq;
import response.LoginResp;
import response.RegisterResp;

import java.util.Objects;

public class LoginService {


    public LoginResp login(LoginReq request, UserDAO users, AuthDAO authTokens) throws DataAccessException {
        if(request.username() == null){
            throw new DataAccessException("unauthorized");
        }
        UserData returningUser = users.getUser(request.username());
        if((returningUser == null) || !BCrypt.checkpw(request.password(), returningUser.password())){
            throw new DataAccessException("unauthorized");
        }
        String newAuthToken =  new ServiceUtils().makeToken();
        AuthData newToken = new AuthData(newAuthToken, request.username());
        authTokens.createAuth(newToken);
        return new LoginResp(returningUser.username(), newAuthToken);

    }
}
