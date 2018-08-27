package cothe.security.access;

/**
 * 클라이언트가 요청한 서비스 정보를 가져온다.
 * @author Jeongjin Kim
 * @since 2018. 8. 27.
 */
public interface RequestedServiceMetaExtractor {
    RequestedServiceMeta extractRequestedServiceMeta(Object object);
}
