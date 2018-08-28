package cothe.security.access;

import lombok.Getter;

import java.util.List;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 27.
 */
@Getter
public class ServicePermission {
    private String strategy;
    private List<ServicePermissionByOperations> permissions;
}
