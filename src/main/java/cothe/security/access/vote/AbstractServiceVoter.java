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

    /**
     * AbstractServiceVoter 객체를 생성하기 위해서는 두 오브젝트가 필요합니다. RoleProvider 는 사용자의 권한 도메인 오브젝트를 반환합니다.
     * RequestedServiceMetaExtractor 는 요청한 오브젝트를 기반으로 권한 체크를 할 수 있는 표준화된 클래스(RequestedServiceMeta)로 반환합니다.
     *
     * @param roleProvider roleId를 입력받아 Role 도메인 객체를 생성하여 리턴하는 인터페이스
     * @param requestedServiceMetaExtractor 클라이언트가 요청한 정보를 RequestedServiceMeta 오브젝트를 생성하여 반환하는 인터페이스
     */
    @SuppressWarnings("WeakerAccess")
    protected AbstractServiceVoter(RoleProvider roleProvider, RequestedServiceMetaExtractor requestedServiceMetaExtractor) {
        this.roleProvider = roleProvider;
        this.requestedServiceMetaExtractor = requestedServiceMetaExtractor;
    }

    /**
     * 스프링 시큐리티에서 주는 권한 설정 정보는 무시합니다.
     */
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

    /**
     * permission 의 type 에 따라 어떻게 결과를 낼 것인지 구체적인 전략을 추상 메소드를 구현해서 사용할 수 있습니다.
     */
    abstract int doVote(Authentication authentication, RequestedServiceMeta requestedServiceMeta);

    /**
     * Permission n 개를 파라미터로 넘기면 n 개의 PermissionDescription 으로 반환합니다.
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

    /**
     * Permission 에 정의된 Definition 들을 순회하면서 사용자 요청과 매치된 것을 찾으면 즉시 true 를 반환합니다.
     */
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

    /**
     * Definition 에 정의된 Operation 이 요청한 Operation 를 포함하면 true 를 반환합니다.
     */
    private boolean doesDefinitionHaveRequestedOperation(Definition op, RequestedServiceMeta requestedServiceMeta) {
        return permissionExpressionMatcher(op.getOperation(), requestedServiceMeta.getOperation());
    }

    /**
     * Definition 에 정의된 파라미터를 순회하면서 사용자 요청과 매치되면 true 를 반환합니다.
     */
    private boolean doesDefinitionMatchRequestedParams(Definition definition, RequestedServiceMeta requestedServiceMeta) {
        for (Map<String, String> param : definition.getParams()) {
            if (doesOperationParamMatchRequestedParam(param, requestedServiceMeta.getParams())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 파라미터의 엔트리를 순회하면서 사용자 요청과 매치되면 true 를 반환합니다.
     * Permission 에는 정의가 된 파라미터 엔트리가 요청에 존재하지 않으면 즉시 false 를 반환합니다.
     */
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

    //todo: parent role 에서 permission 가져오는 정책은?
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

    /**
     * permission String 은 <kbd>,</kbd> 로 구분된 리스트일 수 있습니다.
     * 따라서 <kbd>,</kbd> 로 개별 item 으로 분리하여 순회하고 매치되면 즉시 true 를 반환합니다.
     *
     * 개별 표현식 item 이 <kbd>/</kbd> 로 시작하고 <kbd>/</kbd> 로 끝나면 내부 스트링을 정규표현식으로 인식합니다.
     */
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
