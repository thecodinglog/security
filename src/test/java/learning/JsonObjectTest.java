package learning;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.*;
import java.util.Map;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 17.
 */
public class JsonObjectTest {

    @Test
    public void jsonToMap(){
        Gson gson = new Gson();
        try(Reader fr = new InputStreamReader(getClass().getResourceAsStream("/permissionsJson/allowNormal.json"))){
            Map<String, Object> data = gson.fromJson(fr, Map.class);
            System.out.println(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
