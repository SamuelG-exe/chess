package dataaccess.dao;
import dataaccess.DataAccessException;
import model.UserData;

import java.util.Map;


public interface UserDAO {

    public void createUser(UserData userInfo) throws DataAccessException;

    public UserData getUser(String username) throws DataAccessException;

    public void clear();

    public int size();
}
