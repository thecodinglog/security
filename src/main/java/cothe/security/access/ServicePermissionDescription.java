package cothe.security.access;

import java.util.List;
import java.util.Map;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 27.
 */
public class ServicePermissionDescription {
    class OperationPermissionDescription {
        private String operation;
        private List<Map<String, String>> params;
    }

    private String strategy;
    private List<OperationPermissionDescription> conditions;

}
