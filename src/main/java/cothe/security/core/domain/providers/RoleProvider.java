package cothe.security.core.domain.providers;

import cothe.security.core.domain.Role;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 14.
 */
public interface RoleProvider {
    Role getRole(String roleId);
}
