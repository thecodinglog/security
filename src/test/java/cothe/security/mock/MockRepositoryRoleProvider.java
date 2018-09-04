package cothe.security.mock;

import cothe.security.core.domain.Permission;
import cothe.security.core.domain.Role;
import cothe.security.core.domain.providers.RoleProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 31.
 */
public class MockRepositoryRoleProvider implements RoleProvider {
    private Map<String, Role> roles = new HashMap<>();

    @Override
    public Role getRole(String roleId) {
        return roles.get(roleId);
    }

    public void putRole(String roleId, Set<Permission> permissions){
        roles.put(roleId, new Role(roleId, roleId, null, permissions));
    }

}
