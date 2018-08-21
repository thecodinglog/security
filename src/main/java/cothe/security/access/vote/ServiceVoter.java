package cothe.security.access.vote;

import com.google.gson.Gson;
import cothe.security.core.domain.Permission;
import cothe.security.core.domain.Role;
import cothe.security.core.domain.SecuredObjectType;
import cothe.security.core.domain.providers.RoleProvider;
import org.springframework.boot.json.JsonParser;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 17.
 */
public class ServiceVoter implements AccessDecisionVoter<Object> {
    private RoleProvider roleProvider;

    public ServiceVoter(RoleProvider roleProvider) {
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
        Assert.notNull(this.roleProvider, "There is no role provider.");
        Gson gson = new Gson();
        String targetService = (String) object;



        if (!authentication.isAuthenticated()) {
            return ACCESS_DENIED;
        }

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            Role role = roleProvider.getRole(authority.getAuthority());
            if (role == null)
                continue;

            Set<Permission> permissions = Optional.ofNullable(role.getPermissions()).filter(perms -> perms.stream().anyMatch(
                    perm -> perm.getSecuredObject().getSecuredObjectType() == SecuredObjectType.SERVICE
                            && perm.getSecuredObject().getSecuredObjectId().equals(targetService)
            )).orElse(null);

            if(permissions == null)
                continue;

            Permission permission = permissions.stream().findFirst().orElse(null);

            if(permission == null)
                continue;

            Map permissionJson = gson.fromJson(permission.getPermission(), Map.class);

            /*FilterInvocation fi = null;
            HttpServletRequest rq = fi.getHttpRequest();
*/


        }
        return ACCESS_GRANTED;
    }
}
