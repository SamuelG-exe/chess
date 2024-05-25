package service;

import dataaccess.DataAccessException;
import dataaccess.dao.AuthDAO;
import service.ServiceUtils;
import response.LoginResp;

public class LogoutService {
    ServiceUtils utils = new ServiceUtils();

    public void logout(AuthDAO authTokens, String authToken) throws DataAccessException {
        utils.verifyAuth(authTokens, authToken);
        authTokens.deleteAuth(authToken);

    }
}
