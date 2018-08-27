package cothe.security.mock;

import cothe.security.access.RequestedViewMeta;
import cothe.security.access.RequestedViewMetaExtractor;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 27.
 */
public class MockRequestedViewMetaExtractor implements RequestedViewMetaExtractor {
    @Override
    public RequestedViewMeta extractViewMeta(Object object) {
        return new RequestedViewMeta((String)object);
    }
}
