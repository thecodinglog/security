package cothe.security.access;

import cothe.security.core.domain.*;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 6.
 */
public class AccessDecisionTest {
    private User user1;
    private SecuredObject view1;
    private Permission view1Permission1;
    private Role roleAdmin;


    @Before
    public void setUp() throws Exception {

        view1 = SecuredObject.builder()
                .SecuredObjectId("view1")
                .SecuredObjectName("view1")
                .securedObjectType(SecuredObjectType.VIEW)
                .build();

        view1Permission1 = Permission.builder()
                .permissionId("view1Permission1")
                .permissionName("view1Permission1")
                .securedObject(view1)
                .build();

        roleAdmin = Role.builder()
                .roleId("role_admin")
                .roleName("관리자")
                .permissions(
                        Stream.of(view1Permission1).collect(Collectors.toSet())
                ).build();

        user1 = User.builder()
                .userId("cothe")
                .password("pass")
                .enabled(true)
                .roles(
                        Stream.of(roleAdmin).collect(Collectors.toSet())
                ).build();

    }

    @Test
    public void hasRole() {
        //given

        //when then
        assertTrue(user1.getRoles().stream().anyMatch(role -> role.getRoleId().equals("role_admin")));
    }

    @Test
    public void View에접근권한있는지테스트(){
        String targetViewId = "view1";

        assertTrue(user1.getRoles().stream().anyMatch(
                role -> role.getPermissions().stream().anyMatch(
                        permission -> permission.getSecuredObject().getSecuredObjectId().equals(targetViewId)
                )
        ));
    }
    @Test
    public void View에접근권한없는지테스트(){
        String targetViewId = "view2";

        assertFalse(user1.getRoles().stream().anyMatch(
                role -> role.getPermissions().stream().anyMatch(
                        permission -> permission.getSecuredObject().getSecuredObjectId().equals(targetViewId)
                )
        ));
    }
}
