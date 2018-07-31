package cothe.security.core.repositories;

import cothe.security.core.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Jeongjin Kim
 * @since 2018. 7. 27.
 */
public interface UserRepository extends JpaRepository<User, String> {
}
