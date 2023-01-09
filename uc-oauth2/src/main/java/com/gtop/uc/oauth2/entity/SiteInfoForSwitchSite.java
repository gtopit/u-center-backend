package com.gtop.uc.oauth2.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author hongzw
 */
@Data
public class SiteInfoForSwitchSite {

    private Long projectId;

    private String projectNo;

    private String projectName;

    private String remark;

    private boolean isSiteManager;

    private String typeId;

    private Integer orderNo;

    private String appLogo;

    private String environmentFlag;

    private String webUrl;

    private List<Map<String, Object>> functionList;

    private List<Map<String, Object>> moduleList;

    public boolean isIsSiteManager() {
        return isSiteManager;
    }

    public void setIsSiteManager(boolean siteManager) {
        isSiteManager = siteManager;
    }

    @JsonIgnore
    private String clientId;

    public String getTokenUrl() {
        if (clientId != null && projectId != null && !clientId.isEmpty()) {
            return "/sessionBased/switchSite/" + clientId + "/" + projectId;
        }
        return "/error";
    }

    public String getManageUrl() {
        if (clientId != null && projectId != null && !clientId.isEmpty()) {
            return "/sessionBased/switchSiteManager/" + projectId;
        }
        return "/error";
    }

}
