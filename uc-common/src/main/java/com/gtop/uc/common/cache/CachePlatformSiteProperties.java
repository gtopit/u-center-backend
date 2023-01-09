package com.gtop.uc.common.cache;

import org.springframework.context.annotation.Configuration;

/**
 * 缓存中心站点信息
 * @author hongzw
 */
@Configuration
public class CachePlatformSiteProperties {

    /** 平台站点key，即管理其他站点的站点key */
    public static String platformSiteKey;

    /** 平台站点管理页面 URL地址 */
    public static String platformSiteDashboardUrl;

    /** 权限中心 Client ID */
    public static String platformSiteClientId;

    /** 权限中心 Client Secret */
    public static String platformSiteClientSecret;

    /** 资源id */
    public static String resourceIds;

}
