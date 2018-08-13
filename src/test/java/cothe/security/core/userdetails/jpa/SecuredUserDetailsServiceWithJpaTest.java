package cothe.security.core.userdetails.jpa;

import cothe.security.core.domain.Role;
import cothe.security.core.domain.User;
import cothe.security.core.repositories.RoleRepository;
import cothe.security.core.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertTrue;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 13.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SecuredUserDetailsServiceWithJpaTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    SecuredUserDetailsService userDetailsService;

    @Before
    public void ready(){
        roleRepository.save(
                Role.builder()
                        .roleId("adminRole")
                        .roleName("adminRoleName")
                        .build()
        );

        userRepository.save(User.builder()
                .userId("adminTest")
                .password("password")
                .enabled(true)
                .roles(
                        Stream.of(
                                Role.builder()
                                .roleId("adminRole")
                                .roleName("adminRoleName")
                                .build()
                        ).collect(Collectors.toSet())
                )
                .build());
        userDetailsService = new SecuredUserDetailsService(userRepository);
    }

    @Test
    public void getUserDetails(){
        UserDetails userDetails = userDetailsService.loadUserByUsername("adminTest");
        assertTrue(userDetails.getUsername().equals("adminTest"));
    }

}