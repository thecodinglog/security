package cothe.security.core.repositories;

import cothe.security.core.domain.User;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author Jeongjin Kim
 * @since 2018. 7. 27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    @Ignore
    public void JPA로_MySql_접근() {
        //given
        userRepository.save(User.builder()
                .userId("testUser")
                .password("password")
                .enabled(true)
                .build());

        //when
        List<User> users = userRepository.findAll();

        //then
        assertTrue(users.stream().anyMatch(user -> user.getUserId().equals("testUser")));
    }

    @Test
    @Ignore
    public void JDBC로_MySql_접근() {
        //given
        int cnt = jdbcTemplate.queryForObject("select count(*) from user where user_id = ?"
                , new Object[]{"jdbcTestUser"}, Integer.class);

        if(cnt == 0) {
            jdbcTemplate.update("insert into user(user_id,password,enabled) values (?,?,?)"
                    , "jdbcTestUser"
                    , "password"
                    , true);
        }

        //when
        List<User> users = jdbcTemplate.query("select * from user", (rs, rowNum) -> User.builder()
                .userId(rs.getString("user_id"))
                .password(rs.getString("password"))
                .enabled(rs.getBoolean("enabled"))
                .build()
        );

        //then
        assertTrue(users.stream().anyMatch(user -> user.getUserId().equals("jdbcTestUser")));
    }
}