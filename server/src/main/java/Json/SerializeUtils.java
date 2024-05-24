package Json;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


public class SerializeUtils {

    private static final Gson gson = new Gson();

    public static <T> T fromJson(String json, Class<T> classToBeDeserialized) throws JsonSyntaxException {
        return gson.fromJson(json, classToBeDeserialized);
    }

    public static String toJson(Object object) {
        return gson.toJson(object);
    }


}
