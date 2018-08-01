package cothe.security.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 1.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    private String roleId;
    private String roleName;
    private Role parentRole;
}
