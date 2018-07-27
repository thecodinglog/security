package cothe.security.core.repositories;

import cothe.security.core.domain.Users;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author Jeongjin Kim
 * @since 2018. 7. 27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UsersRepositoryTest {
    @Autowired
    UsersRepository usersRepository;

    @Test
    public void JPA로_MySql_접근() {
        //given
        usersRepository.save(Users.builder()
                .userId("testUser")
                .password("password")
                .enabled(true)
                .build());

        //when
        List<Users> users = usersRepository.findAll();

        //then
        assertTrue(users.stream().anyMatch(user -> user.getUserId().equals("testUser")));
    }
}