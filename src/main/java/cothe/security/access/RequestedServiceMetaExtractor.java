package cothe.security.access;

/**
 * 클라이언트가 요청한 정보를 RequestedServiceMeta 오브젝트를 생성하여 반환합니다.
 *
 * @author Jeongjin Kim
 * @since 2018. 8. 27.
 */
public interface RequestedServiceMetaExtractor {
    RequestedServiceMeta extractRequestedServiceMeta(Object object);
}
