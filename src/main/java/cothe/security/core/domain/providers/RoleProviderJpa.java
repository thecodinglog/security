package cothe.security.core.domain.providers;

import cothe.security.core.domain.Role;
import cothe.security.core.repositories.RoleRepository;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 14.
 */
public class RoleProviderJpa implements RoleProvider {
    private RoleRepository roleRepository;

    public RoleProviderJpa(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getRole(String roleId) {
        return roleRepository.findById(roleId).orElse(null);
    }
}
