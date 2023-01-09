package com.gtop.uc.oauth2.service;

import com.gtop.uc.oauth2.entity.UcSecurityBasicUser;
import com.gtop.uc.oauth2.entity.SiteCategoryEx;
import com.gtop.uc.oauth2.entity.SiteSortDTO;
import com.gtop.uc.oauth2.entity.UserSitesInfo;

import java.util.List;

/**
 * @author hongzw
 */
public interface ISiteService {

    List<SiteCategoryEx> getSiteTypes();

    UserSitesInfo getSwitchSiteList(Short appType, UcSecurityBasicUser user, String deptNo);

    void updateSiteOrder(SiteSortDTO param);

}
