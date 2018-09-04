package cothe.security.access.vote;

import cothe.security.access.RequestedViewMeta;
import cothe.security.access.RequestedViewMetaExtractor;
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

    private final RoleProvider roleProvider;
    private final RequestedViewMetaExtractor requestedViewMetaExtractor;

    public ViewVoter(RoleProvider roleProvider, RequestedViewMetaExtractor requestedViewMetaExtractor) {
        this.roleProvider = roleProvider;
        this.requestedViewMetaExtractor = requestedViewMetaExtractor;
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
        Assert.notNull(this.requestedViewMetaExtractor, "There is no RequestedViewExtractor.");

        if (!authentication.isAuthenticated()) {
            return ACCESS_DENIED;
        }

        String targetView = Optional.ofNullable(this.requestedViewMetaExtractor.extractViewMeta(object))
                .map(RequestedViewMeta::getViewName).orElse(null);
        if (targetView == null) {
            return ACCESS_ABSTAIN;
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
