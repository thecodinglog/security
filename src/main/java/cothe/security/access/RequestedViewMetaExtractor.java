package cothe.security.access;

/**
 * Object 에서 view name 을 추출한다. 추출할 수 없으면 null 를 반환한다.
 * @author Jeongjin Kim
 * @since 2018. 8. 27.
 */
public interface RequestedViewMetaExtractor {
    RequestedViewMeta extractViewMeta(Object object);
}
