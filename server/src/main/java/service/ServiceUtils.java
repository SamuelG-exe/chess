package service;


import dataaccess.DataAccessException;
import dataaccess.dao.AuthDAO;
import model.AuthData;

import java.util.UUID;

public class ServiceUtils {

    public String makeToken(){
        return UUID.randomUUID().toString();
    }

    public AuthData verifyAuth(AuthDAO authDB, String token) throws DataAccessException {
        return authDB.getAuth(token);
    }

}
