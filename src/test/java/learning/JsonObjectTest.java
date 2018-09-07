package learning;

import com.google.gson.Gson;
import cothe.security.access.PermissionDescription;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;
import java.util.Map;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 17.
 */
@Ignore
public class JsonObjectTest {


    @Test
    public void jsonToMap(){
        Gson gson = new Gson();
        try(Reader fr = new InputStreamReader(getClass().getResourceAsStream("/permissionsJson/allowNormal.json"),
                "utf-8")){
            Map data = gson.fromJson(fr, Map.class);
            System.out.println(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void jsonToService(){
        Gson gson = new Gson();
        try(Reader fr = new InputStreamReader(getClass().getResourceAsStream("/permissionsJson/allowNormal.json"),
                "utf-8")){
            PermissionDescription data = gson.fromJson(fr, PermissionDescription.class);
            System.out.println(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
