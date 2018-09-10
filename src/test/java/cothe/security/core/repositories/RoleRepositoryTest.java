package cothe.security.core.repositories;

import cothe.security.core.domain.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * @author Jeongjin Kim
 * @since 2018. 9. 7.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleRepositoryTest {
    @Autowired
    RoleRepository roleRepository;

    @Test
    public void loadHierarchicalRole() {
        Role role1 = Role.builder().roleId("role1").build();
        Role role2 = Role.builder().roleId("role2").parentRole(role1).build();
        Role role3 = Role.builder().roleId("role3").parentRole(role2).build();

        roleRepository.save(role1);
        roleRepository.save(role2);
        roleRepository.save(role3);

        Optional<Role> role3_op = roleRepository.findById("role3");

        Role role = role3_op.orElse(null);

        assertEquals(Objects.requireNonNull(role).getParentRole().getParentRole().getRoleId(), "role1");
    }
}