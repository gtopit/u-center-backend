package com.gtop.uc.admin.service;

/**
 * @author hongzw
 */
public interface IAdminInterfaceService {

    /**
     * 校验应用的接口权限
     *
     * @param clientId   应用id
     * @param requestUrl 请求url
     * @return boolean
     */
    boolean check(String clientId, String requestUrl);

    /**
     * 根据资源id缓存应用权限
     *
     * @param resourceId 资源id
     */
    void buildCacheByResource(String resourceId);

}
