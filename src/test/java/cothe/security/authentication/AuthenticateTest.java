package cothe.security.authentication;

import cothe.security.core.domain.Role;
import cothe.security.core.domain.User;
import cothe.security.core.repositories.UserRepository;
import cothe.security.core.userdetails.MockUserDetailsService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * @author Jeongjin Kim
 * @since 2018. 7. 31.
 */

public class AuthenticateTest {

    private UserRepository userRepository;
    private UserDetailsService userDetailsService;
    private DaoAuthenticationProvider authenticationProvider;
    private List<AuthenticationProvider> authenticationProviders;
    private ProviderManager providerManager;

    @Before
    public void setUp() throws Exception {

        userRepository = new MockUserRepository();
        userRepository.save(
                new User("cothe", "pass", true,
                        Stream.of(Role.builder()
                                .roleId("admin")
                                .roleName("관리자").parentRole(null)
                                .build()).collect(Collectors.toSet())
                ));

        userDetailsService = new MockUserDetailsService(userRepository);

        authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        authenticationProviders = Stream.of(
                authenticationProvider
        ).collect(Collectors.toList());

        providerManager = new ProviderManager(authenticationProviders);
    }

    @Test
    public void 인증성공() {
        Authentication auth = new UsernamePasswordAuthenticationToken("cothe", "pass");

        assertFalse(auth.isAuthenticated());
        auth = providerManager.authenticate(auth);
        assertTrue(auth.isAuthenticated());
    }

    @Test(expected = BadCredentialsException.class)
    public void 인증실패() {
        Authentication auth = new UsernamePasswordAuthenticationToken("cothe1", "pass");

        auth = providerManager.authenticate(auth);
    }
}
