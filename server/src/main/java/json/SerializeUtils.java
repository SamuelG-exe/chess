package json;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import response.ErrorMessagesResp;


public class SerializeUtils {

    private static final Gson Gson = new Gson();

    public static <T> T fromJson(String json, Class<T> classToBeDeserialized) throws JsonSyntaxException {
        return Gson.fromJson(json, classToBeDeserialized);
    }

    public static String toJson(Object object) {
        return Gson.toJson(object);
    }

}
