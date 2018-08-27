package cothe.security.access;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 27.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestedServiceMeta {
    private String serviceName;
    private String operation;
    private Map<String, String> params;
}
