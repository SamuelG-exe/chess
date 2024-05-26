package dataaccess.dao.internalDAO;

import dataaccess.DataAccessException;
import dataaccess.dao.UserDAO;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class UserInternalDAO implements UserDAO {
    private final Map<String, UserData> allUsers = new HashMap<>();


    @Override
    public void createUser(UserData userInfo) throws DataAccessException {
        if(allUsers.containsKey(userInfo.username())){
            throw new DataAccessException("already taken");
        };
        allUsers.put(userInfo.username(), userInfo);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        UserData neededUser = allUsers.get(username);
        if(neededUser==null){
            throw new DataAccessException("unauthorized");
        }

        return neededUser;

    }

    @Override
    public void clear() {
        allUsers.clear();
    }
}
