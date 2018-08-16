package cothe.security.mock;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 10.
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockSecuredUserSecurityContextFactory.class)
public @interface WithMockSecuredUser {
    String username() default "user1";
    String name() default "user one";
    String roles() default "role1,role2";
}
