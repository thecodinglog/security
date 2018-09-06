package cothe.security.core.domain.providers;

import cothe.security.core.domain.Role;

/**
 * roleId를 입력받아 Role 도메인 객체를 생성하여 리턴합니다.
 *
 * @author Jeongjin Kim
 * @since 2018. 8. 14.
 */
public interface RoleProvider {
    Role getRole(String roleId);
}
