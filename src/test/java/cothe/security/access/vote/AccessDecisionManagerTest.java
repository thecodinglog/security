package cothe.security.access.vote;

import cothe.security.core.domain.Permission;
import cothe.security.core.domain.SecuredObject;
import cothe.security.core.domain.SecuredObjectType;
import cothe.security.mock.MockRoleProvider;
import cothe.security.mock.MockRequestedViewMetaExtractor;
import cothe.security.mock.WithMockSecuredUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 16.
 */
@RunWith(SpringRunner.class)
@WithMockSecuredUser(username = "admin", name = "admin", roles = "default_view_permission")
public class AccessDecisionManagerTest {
    private Set<Permission> permissions;
    private Authentication authentication;
    private ViewVoter viewVoter;
    private AccessDecisionManager accessDecisionManager;

    @Before
    public void setUp() {
        permissions = Stream.of(
                new Permission("default_view_permission", "default_view_permission", null,
                        new SecuredObject("default_object", "default_object", SecuredObjectType.VIEW))
        ).collect(Collectors.toSet());
        viewVoter = new ViewVoter(new MockRoleProvider(permissions), new MockRequestedViewMetaExtractor());
        authentication = SecurityContextHolder.getContext().getAuthentication();
        accessDecisionManager = new AffirmativeBased(
                Arrays.asList(
                        viewVoter
                )
        );
    }

    @Test
    public void decideTest() {
        String targetView = "default_object";
        accessDecisionManager.decide(authentication, targetView, null);

    }
}
