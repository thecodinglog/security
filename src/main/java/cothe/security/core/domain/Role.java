package cothe.security.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 1.
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    private String roleId;
    private String roleName;

    @ManyToOne()
    @JoinColumn(name = "parent_user_id")
    private Role parentRole;
}
