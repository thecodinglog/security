package cothe.security.access.vote;

import cothe.security.access.PermissionDescription;
import cothe.security.access.RequestedServiceMeta;
import cothe.security.access.RequestedServiceMetaExtractor;
import cothe.security.core.domain.providers.RoleProvider;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cothe.security.access.PermissionType.PERMISSION_TYPE_ALLOW;
import static cothe.security.access.PermissionType.PERMISSION_TYPE_DENY;

/**
 * 사용자가 가지고 있는 Permission 중 cothe.security.access.PermissionType#PERMISSION_TYPE_DENY 타입에 매치되는 것이 있으면
 * 즉시 org.springframework.security.access.AccessDecisionVoter#ACCESS_DENIED 를 반환한다.
 *
 * @author Jeongjin Kim
 * @since 2018. 8. 30.
 */
public class DenialFirstServiceVoter extends AbstractServiceVoter {
    public DenialFirstServiceVoter(RoleProvider roleProvider, RequestedServiceMetaExtractor requestedServiceMetaExtractor) {
        super(roleProvider, requestedServiceMetaExtractor);
    }

    @Override
    int doVote(Authentication authentication, RequestedServiceMeta requestedServiceMeta) {
        List<PermissionDescription> permissionDescriptions = parsePermissionDescriptionScripts(extractPermissions(authentication, requestedServiceMeta))
                .stream().filter(Objects::nonNull).sorted((o1, o2) -> {
                    if (o1.getPermissionType().equals(PERMISSION_TYPE_DENY) && o2.getPermissionType().equals(PERMISSION_TYPE_ALLOW)) {
                        return -1;
                    } else if (o1.getPermissionType().equals(PERMISSION_TYPE_ALLOW) && o2.getPermissionType().equals(PERMISSION_TYPE_DENY)) {
                        return 1;
                    } else {
                        return 0;
                    }
                }).collect(Collectors.toList());

        for (PermissionDescription permissionDescription : permissionDescriptions) {
            if (doesPermissionDescriptionMatchRequestedService(permissionDescription, requestedServiceMeta)) {
                if (permissionDescription.getPermissionType().equals(PERMISSION_TYPE_DENY)) {
                    return ACCESS_DENIED;
                } else if (permissionDescription.getPermissionType().equals(PERMISSION_TYPE_ALLOW)) {
                    return ACCESS_GRANTED;
                }
            }
        }

        return ACCESS_DENIED;
    }

}
