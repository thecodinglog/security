package cothe.security.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 6.
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecuredObject {
    @Id
    private String SecuredObjectId;
    private String SecuredObjectName;

    @Enumerated(value = EnumType.STRING)
    private SecuredObjectType securedObjectType;
}
