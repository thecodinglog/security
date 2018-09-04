package cothe.security.access.vote;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import cothe.security.access.Definition;
import cothe.security.access.PermissionDescription;
import cothe.security.access.RequestedServiceMeta;
import cothe.security.access.RequestedServiceMetaExtractor;
import cothe.security.core.domain.Permission;
import cothe.security.core.domain.Role;
import cothe.security.core.domain.SecuredObjectType;
import cothe.security.core.domain.providers.RoleProvider;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 17.
 */
@SuppressWarnings("WeakerAccess")
@Slf4j
public abstract class AbstractServiceVoter implements AccessDecisionVoter<Object> {
    private final RoleProvider roleProvider;
    private final RequestedServiceMetaExtractor requestedServiceMetaExtractor;
    private static final Gson gson = new Gson();

    @SuppressWarnings("WeakerAccess")
    protected AbstractServiceVoter(RoleProvider roleProvider, RequestedServiceMetaExtractor requestedServiceMetaExtractor) {
        this.roleProvider = roleProvider;
        this.requestedServiceMetaExtractor = requestedServiceMetaExtractor;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        Assert.notNull(this.roleProvider, "There is no role provider.");
        Assert.notNull(this.requestedServiceMetaExtractor, "There is no RequestedServiceMetaExtractor.");

        if (!authentication.isAuthenticated()) {
            return ACCESS_DENIED;
        }

        RequestedServiceMeta requestedServiceMeta = this.requestedServiceMetaExtractor.extractRequestedServiceMeta(object);

        return doVote(authentication, requestedServiceMeta);

    }

    abstract int doVote(Authentication authentication, RequestedServiceMeta requestedServiceMeta);

    /**
     * Permission n 개를 파라미터로 넘기면 n 개의 PermissionDescription 으로 반환합니다.
     *
     * parsing 할 permission json string 이 null 이거나 형식에 맞지 않으면 null 을 반환 리스트에 추가합니다.
     */
    @SuppressWarnings("WeakerAccess")
    protected List<PermissionDescription> parsePermissionDescriptionScripts(Set<Permission> permissions) {
        List<PermissionDescription> permissionDescriptions = new ArrayList<>();
        PermissionDescription permissionDescription;
        for (Permission permission : permissions) {
            permissionDescription = null;
            try {
                permissionDescription = gson.fromJson(permission.getPermission(), PermissionDescription.class);
            } catch (JsonSyntaxException e) {
                log.error("권한정보를 읽는 중 오류가 발생했습니다. Permission[{}]가 JSON 형식인지 확인하세요.", permission.getPermissionId());
            }
            permissionDescriptions.add(permissionDescription);
        }

        return permissionDescriptions;
    }

    @SuppressWarnings("WeakerAccess")
    protected boolean doesPermissionDescriptionMatchRequestedService(
            @NonNull PermissionDescription permissionDescription
            , @NonNull RequestedServiceMeta requestedServiceMeta) {
        List<Definition> definitions = permissionDescription.getDefinitions();
        for (Definition definition : definitions) {
            // 사용자가 요청한 Operation 을 definition 이 정의하고 있는지 확인
            if (doesDefinitionHaveRequestedOperation(definition, requestedServiceMeta)) {
                // Definition 에 정의된 파라미터와 요청된 파라미터가 매치되는지 확인,
                if (doesDefinitionMatchRequestedParams(definition, requestedServiceMeta)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean doesDefinitionHaveRequestedOperation(Definition op, RequestedServiceMeta requestedServiceMeta) {
        return permissionExpressionMatcher(op.getOperation(), requestedServiceMeta.getOperation());
    }

    private boolean doesDefinitionMatchRequestedParams(Definition definition, RequestedServiceMeta requestedServiceMeta) {
        List<Map<String, String>> params = definition.getParams();
        for (Map<String, String> param : params) {
            if (doesOperationParamMatchRequestedParam(param, requestedServiceMeta.getParams())) {
                return true;
            }
        }
        return false;
    }

    private boolean doesOperationParamMatchRequestedParam(Map<String, String> permissionParam, Map<String, String> requestedParam) {
        for (Map.Entry<String, String> entry : permissionParam.entrySet()) {
            String requestedValue = requestedParam.get(entry.getKey());
            if (requestedValue == null) {
                return false;
            }

            if (!doesParamMatchRequestedParam(entry.getValue(), requestedValue))
                return false;
        }
        return true;
    }

    private boolean doesParamMatchRequestedParam(String permissionParamEntry, String requestedValue) {
        return permissionExpressionMatcher(permissionParamEntry, requestedValue);
    }

    /**
     * 사용자는 여러 Role 을 가질 수 있기 때문에 Role 이 가지고 있는 퍼미션이 중복될 수 있다.
     * extractPermissions 는 롤들이 가지고 있는 퍼미션을 모두 가져와서 중복되지 않는 Set 타입으로 반환한다.
     */
    @SuppressWarnings("WeakerAccess")
    protected Set<Permission> extractPermissions(Authentication authentication, RequestedServiceMeta requestedServiceMeta) {

        Set<Permission> permissions = new HashSet<>();

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            Role role = roleProvider.getRole(authority.getAuthority());
            if (role == null)
                continue;

            Optional.ofNullable(role.getPermissions()).filter(perms -> perms.stream().anyMatch(
                    perm -> perm.getSecuredObject().getSecuredObjectType() == SecuredObjectType.SERVICE
                            && perm.getSecuredObject().getSecuredObjectId().equals(requestedServiceMeta.getServiceName())
            )).ifPresent(permissions::addAll);
        }
        return permissions;
    }

    private boolean permissionExpressionMatcher(String permissionExpression, String targetValue) {
        String[] permissionParamEntryValues = permissionExpression.split(",");
        for (String permissionParamEntryValue : permissionParamEntryValues) {
            if (permissionParamEntryValue.startsWith("/") && permissionParamEntryValue.endsWith("/")) {
                String rex = permissionParamEntryValue.substring(1, permissionParamEntryValue.length() - 1);
                if (targetValue.matches(rex)) {
                    return true;
                }

            } else {
                if (permissionParamEntryValue.equals(targetValue)) {
                    return true;
                }
            }
        }
        return false;
    }
}
