package service;

import java.util.UUID;

public class ServiceUtils {

    public String makeToken(){
        return UUID.randomUUID().toString();
    }

}
