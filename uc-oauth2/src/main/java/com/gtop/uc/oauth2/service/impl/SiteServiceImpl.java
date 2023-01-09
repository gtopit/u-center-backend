package com.gtop.uc.oauth2.service.impl;

import com.gtop.uc.common.constant.GlobalProperties;
import com.gtop.uc.oauth2.dao.SysSiteMapper;
import com.gtop.uc.oauth2.entity.*;
import com.gtop.uc.oauth2.service.ISiteService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author hongzw
 */
@Service
public class SiteServiceImpl implements ISiteService {

    @Resource
    private SysSiteMapper sysSiteMapper;

    @Override
    public List<SiteCategoryEx> getSiteTypes() {
        return sysSiteMapper.selectSiteCategories();
    }

    @Override
    public UserSitesInfo getSwitchSiteList(Short appType, UcSecurityBasicUser user, String deptNo) {
        UserSitesInfo userSitesInfo = new UserSitesInfo();
        // 获取站点类型列表
        List<SiteCategoryEx> siteCategories = getSiteTypes();
        if (CollectionUtils.isEmpty(siteCategories)) {
            userSitesInfo.setSiteCategories(new ArrayList<>());
            return userSitesInfo;
        }
        // 获取用户关联站点列表
        List<SiteEx> sites = sysSiteMapper.listByUserNo(user.getUsername(), appType, deptNo);

        // 将站点归类，并返回平台站点（返回再设置值，只是为了可阅读性，在categorizeSitesAndReturnUserInfo方法里设置UserInfo也可以）
        SiteEx platformSite = categorizeSitesAndReturnPlatformSite(siteCategories, sites);

        setUserInfo(userSitesInfo, user, platformSite);

        userSitesInfo.setSiteCategories(siteCategories);

        return userSitesInfo;
    }

    @Override
    public void updateSiteOrder(SiteSortDTO param) {
        sysSiteMapper.updateSort(param.getSiteKey(), new Date());
    }

    private void setUserInfo(UserSitesInfo userSitesInfo, UcSecurityBasicUser user, SiteEx platformSite) {
        UserSitesInfo.Base base = new UserSitesInfo.Base(user);
        base.setPlatformSite(platformSite);
        userSitesInfo.setUserInfo(base);
    }

    private SiteEx categorizeSitesAndReturnPlatformSite(List<SiteCategoryEx> siteCategories, List<SiteEx> sites) {

        if (CollectionUtils.isEmpty(sites)) {
            return null;
        }

        SiteEx platformSite = null;

        Map<Integer, SiteCategoryEx> categoryMap = new HashMap<>(siteCategories.size());
        for (SiteCategoryEx siteCategory : siteCategories) {
            siteCategory.setSites(new ArrayList<>());
            categoryMap.put(siteCategory.getId(), siteCategory);
        }

        for (SiteEx site : sites) {
            if (GlobalProperties.PLATFORM_SITE_KEY.equals(site.getSiteKey())) {
                platformSite = site;
                continue;
            }
            categoryMap.get(site.getCateId()).getSites().add(site);
        }

        return platformSite;

    }

}
