package com.gtop.uc.common.cache;

import com.gtop.uc.common.cache.dao.SiteMapper;
import com.gtop.uc.common.constant.GlobalProperties;
import com.gtop.uc.common.entity.dto.SysSiteSDTO;
import com.gtop.uc.common.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author hongzw
 */
@Component
@Order(1)
@Slf4j
public class Oauth2SiteInitialization implements CommandLineRunner {

    @Resource
    private SiteMapper siteMapper;

    @Override
    public void run(String... arg0) {
        log.info("开始获取自定义参数配置...");
        try {
            SysSiteSDTO sysSite = siteMapper.selectSiteBySiteKey(GlobalProperties.PLATFORM_SITE_KEY);
            if (sysSite == null) {
                log.error("通过PLATFORM_SITE_KEY:{} 获取平台管控站点信息失败！！！", GlobalProperties.PLATFORM_SITE_KEY);
            } else {
                CachePlatformSiteProperties.platformSiteKey = GlobalProperties.PLATFORM_SITE_KEY;
                CachePlatformSiteProperties.platformSiteDashboardUrl = GlobalProperties.PLATFORM_SITE_DASHBOARD_URL;
                CachePlatformSiteProperties.platformSiteClientId = sysSite.getClientId();
                CachePlatformSiteProperties.platformSiteClientSecret = sysSite.getClientSecret();
                CachePlatformSiteProperties.resourceIds = sysSite.getResourceIds();
            }
        } catch (Exception e) {
            log.error("配置自定义参数时发生异常！{}" + e.getMessage());
            throw new ApplicationException(e);
        } finally {
            log.info("自定义参数配置结束");
        }
    }

}
