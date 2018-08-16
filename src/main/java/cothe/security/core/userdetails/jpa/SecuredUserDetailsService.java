package cothe.security.core.userdetails.jpa;

import cothe.security.core.repositories.UserRepository;
import cothe.security.core.userdetails.SecuredUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 13.
 */
public class SecuredUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    public SecuredUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserDetails> userDetails = userRepository.findById(username).map(user ->
                SecuredUser.builder()
                        .username(user.getUserId())
                        .password(user.getPassword())
                        .authorities(user.getRoles().stream().
                                map(role -> new SimpleGrantedAuthority(role.getRoleId())).collect(Collectors.toList()))
                        .build()
        );

        return userDetails.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
}
