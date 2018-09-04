package cothe.security.access;

import lombok.Getter;

import java.util.List;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 27.
 */
@Getter
public class PermissionDescription {
    private final String permissionType;
    private final List<Definition> definitions;

    public PermissionDescription(String permissionType, List<Definition> definitions) {
        this.permissionType = permissionType;
        this.definitions = definitions;
    }
}
