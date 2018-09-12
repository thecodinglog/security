package cothe.security.core.domain.providers;

import cothe.security.core.domain.Role;
import cothe.security.core.exceptions.RoleNotFoundException;

/**
 * roleId를 입력받아 Role 도메인 객체를 생성하여 리턴합니다.
 * 입력된 Role Id에 해당하는 Role 객체를 가져올 수 없을 때 RoleNotFoundException 이 발생합니다.
 * roleId는 null 일 수 없습니다. 만약 null 이면 IllegalArgumentException 을 발생합니다.
 *
 * @author Jeongjin Kim
 * @since 2018. 8. 14.
 */
public interface RoleProvider {
    Role getRole(String roleId) throws RoleNotFoundException;
}
