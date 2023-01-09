package com.gtop.uc.oauth2.entity;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 对外的用户基本信息对象
 * @author hongzw
 */
@Data
@NoArgsConstructor
public class UserInfoForSwitchSite {

    public UserInfoForSwitchSite(UcSecurityBasicUser user) {
        this.setUserNo(user.getUserNo());
        this.setUsername(user.getUsername());
        this.setPlatformManager(user.isPlatformManager());

    }

    private String userNo;

    private String username;

    private boolean isPlatformManager;

    private SiteInfoForSwitchSite platformSite;

    public boolean isPlatformManager() { //只是为了让传给前端的参数展示为“isPlatformManager”
        return isPlatformManager;
    }

    public void setPlatformManager(boolean platformManager) {
        isPlatformManager = platformManager;
    }

}
