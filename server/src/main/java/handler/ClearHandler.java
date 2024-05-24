package handler;

import spark.Request;
import spark.Response;
import spark.Route;
import Json.SerializeUtils;
import service.ClearDBService;

public class ClearHandler implements Route {


    ClearDBService cleanSlate = new ClearDBService();

    ErrorMessages errorMessage = new ErrorMessages();

    @Override
    public Object handle(Request request, Response response) throws Exception {
        try {
            cleanSlate.clearDB();
            response.status(200);
            return SerializeUtils.toJson(new Object());
        }
        catch (Exception e) {
            response.status(500);
            return SerializeUtils.toJson(errorMessage.message = ("Error: " + e.getMessage()));
        }

    }


}
