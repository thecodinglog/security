package cothe.security.mock;

import cothe.security.access.RequestedServiceMeta;
import cothe.security.access.RequestedServiceMetaExtractor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 31.
 */
public class MockRequestedServiceMetaExtractor implements RequestedServiceMetaExtractor {
    @Override
    public RequestedServiceMeta extractRequestedServiceMeta(Object object) {
        if(object instanceof RequestedServiceMeta){
            return (RequestedServiceMeta) object;
        }else {
            Map<String, String> param = new HashMap<>();
            param.put("device","mobile");
            RequestedServiceMeta requestedServiceMeta = new RequestedServiceMeta(
                    "service1",
                    "save",
                    param
            );
            return requestedServiceMeta;
        }
    }
}
