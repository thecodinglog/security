package cothe.security.core.domain.providers;

import cothe.security.core.domain.Role;
import cothe.security.core.exceptions.RoleNotFoundException;
import cothe.security.core.repositories.RoleRepository;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 14.
 */
public class RoleProviderJpa implements RoleProvider {
    private final RoleRepository roleRepository;

    public RoleProviderJpa(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getRole(String roleId) {
        if (roleId == null) throw new IllegalArgumentException();

        return roleRepository.findById(roleId).orElseThrow(() -> new RoleNotFoundException(
                String.format("Can't find role id : %s", roleId)));
    }
}
