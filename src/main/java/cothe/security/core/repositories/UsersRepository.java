package cothe.security.core.repositories;

import cothe.security.core.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Jeongjin Kim
 * @since 2018. 7. 27.
 */
public interface UsersRepository extends JpaRepository<Users, Long> {
}
