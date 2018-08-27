package cothe.security.mock;

import cothe.security.access.ViewNameExtractor;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 27.
 */
public class MockViewNameExtractor implements ViewNameExtractor {
    @Override
    public String extractViewName(Object object) {
        return (String) object;
    }
}
