package cothe.security.access.vote;

import cothe.security.access.ViewNameExtractor;
import cothe.security.core.domain.Role;
import cothe.security.core.domain.SecuredObjectType;
import cothe.security.core.domain.providers.RoleProvider;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 10.
 */
public class ViewVoter implements AccessDecisionVoter<Object> {

    private RoleProvider roleProvider;
    private ViewNameExtractor viewNameExtractor;

    public ViewVoter(RoleProvider roleProvider, ViewNameExtractor viewNameExtractor) {
        this.roleProvider = roleProvider;
        this.viewNameExtractor = viewNameExtractor;
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
        Assert.notNull(this.roleProvider, "There is no role provider.");
        Assert.notNull(this.viewNameExtractor, "There is no view name extractor.");

        String targetView = this.viewNameExtractor.extractViewName(object);

        if (!authentication.isAuthenticated()) {
            return ACCESS_DENIED;
        }

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            Role role = roleProvider.getRole(authority.getAuthority());

            if (role == null)
                continue;

            if (Optional.ofNullable(role.getPermissions())
                    .map(permissions -> permissions.stream()
                            .filter(permission -> permission.getSecuredObject().getSecuredObjectType() == SecuredObjectType.VIEW)
                            .anyMatch(permission -> permission.getSecuredObject().getSecuredObjectId().equals(targetView))
                    ).orElse(false)) {
                return ACCESS_GRANTED;
            }
        }
        return ACCESS_DENIED;
    }
}
