package cothe.security.mock;

import cothe.security.core.repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Jeongjin Kim
 * @since 2018. 7. 31.
 */
public class MockUserDetailsService implements UserDetailsService {
    UserRepository userRepository;

    public MockUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        Optional<UserDetails> userDetails = userRepository.findById(username).map(user ->
                User.builder()
                        .username(user.getUserId())
                        .password(user.getPassword())
                        .authorities(user.getRoles().stream().
                                map(role -> new SimpleGrantedAuthority(role.getRoleId())).collect(Collectors.toList()))
                        .build()
        );

        return userDetails.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

    }
}