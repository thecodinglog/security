package cothe.security.access.vote;

import cothe.security.core.domain.Permission;
import cothe.security.core.domain.SecuredObject;
import cothe.security.core.domain.SecuredObjectType;
import cothe.security.mock.MockRoleProvider;
import cothe.security.mock.MockViewNameExtractor;
import cothe.security.mock.WithMockSecuredUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertTrue;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 14.
 */
@RunWith(SpringRunner.class)
@WithMockSecuredUser(username = "admin", name = "admin", roles = "default_view_permission")
public class ViewVoterTest {

    private Set<Permission> permissions;
    private Authentication authentication;
    private ViewVoter viewVoter;

    @Before
    public void setUp() {
        permissions = Stream.of(
                new Permission("default_view_permission", "default_view_permission", null,
                        new SecuredObject("default_object", "default_object", SecuredObjectType.VIEW))
        ).collect(Collectors.toSet());

        authentication = SecurityContextHolder.getContext().getAuthentication();
        viewVoter = new ViewVoter(new MockRoleProvider(permissions), new MockViewNameExtractor());
    }

    @Test
    public void view접근권한있을때(){
        String targetView = "default_object";
        int voteResult = viewVoter.vote(authentication, targetView, null);
        assertTrue(voteResult > 0);
    }

    @Test
    public void view접근권한없을때(){
        String targetView = "view1";
        int voteResult = viewVoter.vote(authentication, targetView, null);
        assertTrue(voteResult < 0);
    }

    @Test
    public void permission이null이면(){
        ViewVoter nullPermissionViewVoter = new ViewVoter(new MockRoleProvider(null), new MockViewNameExtractor());

        String targetView = "default_object";
        int voteResult = nullPermissionViewVoter.vote(authentication, targetView, null);
        assertTrue(voteResult < 0);
    }
}