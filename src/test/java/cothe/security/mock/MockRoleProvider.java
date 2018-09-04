package cothe.security.mock;

import cothe.security.core.domain.Permission;
import cothe.security.core.domain.Role;
import cothe.security.core.domain.SecuredObject;
import cothe.security.core.domain.SecuredObjectType;
import cothe.security.core.domain.providers.RoleProvider;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 14.
 */
public class MockRoleProvider implements RoleProvider {
    private Role role;
    private Set<Permission> permissions;

    /**
     * 생성자로 받은 Permission 를 요청한 Role Id 와 묶어서 Role 를 반환한다.
     */
    @Override
    public Role getRole(String roleId) {
        role = new Role(roleId, roleId, null, permissions);

        return role;
    }

    public MockRoleProvider() {
        permissions = Stream.of(
                new Permission("default_view_permission", "default_view_permission", null,
                        new SecuredObject("default_object", "default_object", SecuredObjectType.VIEW))
        ).collect(Collectors.toSet());
    }

    public MockRoleProvider(Set<Permission> permissions) {
        this.permissions = permissions;

    }
}
