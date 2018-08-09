package cothe.security.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 6.
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {
    @Id
    private String permissionId;
    private String permissionName;
    private String permission;
    @ManyToOne
    @JoinColumn(name = "secured_object_id")
    private SecuredObject securedObject;

}
