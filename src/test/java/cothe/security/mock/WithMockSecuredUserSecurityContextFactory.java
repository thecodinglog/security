package cothe.security.mock;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 10.
 */
public class WithMockSecuredUserSecurityContextFactory implements WithSecurityContextFactory<WithMockSecuredUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockSecuredUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Authentication auth = new UsernamePasswordAuthenticationToken(annotation.username(), "pass",
                Arrays.stream(annotation.roles().
                        split(",")).map(s -> new SimpleGrantedAuthority(s)).collect(Collectors.toSet()));
        context.setAuthentication(auth);

        return context;
    }
}
