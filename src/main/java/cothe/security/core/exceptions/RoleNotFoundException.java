package cothe.security.core.exceptions;

/**
 * @author Jeongjin Kim
 * @since 2018. 9. 11.
 */
public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
