package service;

import dataaccess.DataAccessException;
import dataaccess.dao.AuthDAO;
import dataaccess.dao.UserDAO;
import org.mindrot.jbcrypt.BCrypt;
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
        String hashedPassword = BCrypt.hashpw(request.password(), BCrypt.gensalt());
        newUser = new UserData(request.username(), hashedPassword, request.email());
        newAuth = new AuthData(utils.makeToken(), request.username());
        if(users.getUser(newUser.username()) != null){
            throw new DataAccessException("already taken");
        }

        users.createUser(newUser);
        authTokens.createAuth(newAuth);

        return new RegisterResp(newUser.username(), newAuth.authToken());
    }
}
