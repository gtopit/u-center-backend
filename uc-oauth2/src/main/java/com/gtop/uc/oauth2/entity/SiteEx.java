package com.gtop.uc.oauth2.entity;

import cn.hutool.core.util.StrUtil;
import com.gtop.uc.common.entity.sys.SysSite;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hongzw
 */
public class SiteEx extends SysSite {

    @Getter
    @Setter
    private String homepage;

    @Setter
    private Boolean siteManager;

    public Boolean isSiteManager() {
        return siteManager;
    }

    @Setter
    @Getter
    private String appLogo;

    public String getTokenUrl() {
        if (StrUtil.isNotBlank(getClientId()) && StrUtil.isNotBlank(getSiteKey())) {
            return "/sessionBased/switchSite/" + getClientId() + "/" + getSiteKey();
        }
        return "/error";
    }

    public String getManageUrl() {
        if (StrUtil.isNotBlank(getClientId()) && StrUtil.isNotBlank(getSiteKey())) {
            return "/sessionBased/switchSiteManager/" + getSiteKey();
        }
        return "/error";
    }

}
