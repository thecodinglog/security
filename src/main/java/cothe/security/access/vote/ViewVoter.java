package cothe.security.access.vote;

import cothe.security.core.domain.Role;
import cothe.security.core.domain.SecuredObjectType;
import cothe.security.core.domain.providers.RoleProvider;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 10.
 */
public class ViewVoter implements AccessDecisionVoter<Object> {

    private RoleProvider roleProvider;

    public ViewVoter(RoleProvider roleProvider) {
        this.roleProvider = roleProvider;
    }


    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        String targetView = (String) object;

        if (!authentication.isAuthenticated()) {
            return ACCESS_DENIED;
        }

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            Role role = roleProvider.getRole(authority.getAuthority());

            if (role == null)
                continue;

            if (role.getPermissions().stream()
                    .filter(permission -> permission.getSecuredObject().getSecuredObjectType()== SecuredObjectType.VIEW)
                    .anyMatch(permission -> permission.getSecuredObject().getSecuredObjectId().equals(targetView))) {
                return ACCESS_GRANTED;
            }
        }

        return ACCESS_DENIED;
    }
}
