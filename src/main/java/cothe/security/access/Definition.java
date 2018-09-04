package cothe.security.access;

import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 28.
 */
@Getter
public class Definition {
    /**
     * <kbd>,</kbd>로 구분된 Operation 이 올 수 있다.
     */
    private final String operation;
    private final List<Map<String, String>> params;

    public Definition(String operation, List<Map<String, String>> params) {
        this.operation = operation;
        this.params = params;
    }
}