package service;


import dataaccess.DataAccessException;
import dataaccess.dao.AuthDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ServiceUtils {
    private List<Integer> usedNumbers = new ArrayList<>();
    private int max = 100;

    public String makeToken(){
        return UUID.randomUUID().toString();
    }

    public void verifyAuth(AuthDAO authDB, String token) throws DataAccessException {
        authDB.getAuth(token);
    }

    public String newID(){
        Random randNumber = new Random();
        int newNumber = randNumber.nextInt(max);
        for(int number : usedNumbers){
            if(number == newNumber){
                newNumber=Integer.parseInt(newID());
                max*=2;
            }
        }
        usedNumbers.add(newNumber);
        return Integer.toString(newNumber);
    }

}
