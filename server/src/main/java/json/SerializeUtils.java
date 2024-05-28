package json;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


public class SerializeUtils {

    private static final Gson GSON = new Gson();

    public static <T> T fromJson(String json, Class<T> classToBeDeserialized) throws JsonSyntaxException {
        return GSON.fromJson(json, classToBeDeserialized);
    }

    public static String toJson(Object object) {
        return GSON.toJson(object);
    }

}
