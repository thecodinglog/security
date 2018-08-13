package cothe.security.core.repositories;

import cothe.security.core.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 13.
 */
public interface RoleRepository extends JpaRepository<Role, String> {
}
