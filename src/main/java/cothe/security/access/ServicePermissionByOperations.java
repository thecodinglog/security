package cothe.security.access;

import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 28.
 */
@Getter
public class ServicePermissionByOperations {
    /**
     * <kbd>,</kbd>로 구분된 Operation이 올 수 있다.
     */
    private String operations;
    private List<Map<String, String>> params;

}