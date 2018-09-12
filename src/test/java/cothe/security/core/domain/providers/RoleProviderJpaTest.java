package cothe.security.core.domain.providers;

import cothe.security.core.domain.Role;
import cothe.security.core.exceptions.RoleNotFoundException;
import cothe.security.core.repositories.RoleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author Jeongjin Kim
 * @since 2018. 9. 12.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleProviderJpaTest {
    @Autowired
    RoleRepository roleRepository;

    RoleProvider roleProvider;

    @Before
    public void setUp(){
        roleProvider = new RoleProviderJpa(roleRepository);

        Role role1 = Role.builder().roleId("role1").build();
        Role role2 = Role.builder().roleId("role2").parentRole(role1).build();
        Role role3 = Role.builder().roleId("role3").parentRole(role2).build();

        roleRepository.save(role1);
        roleRepository.save(role2);
        roleRepository.save(role3);

    }
    @Test
    public void bringRole(){
        Role role = roleProvider.getRole("role3");
        assertEquals(role.getRoleId(), "role3");
    }
    @Test(expected = RoleNotFoundException.class)
    public void bringRoleException(){
        Role role = roleProvider.getRole("role4");

    }

}