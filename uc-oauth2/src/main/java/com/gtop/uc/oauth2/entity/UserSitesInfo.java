package com.gtop.uc.oauth2.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 * @Description 用户所属站点类型列表信息
 * @author hongzw
 **/
@Data
public class UserSitesInfo {

    private Base userInfo;

    private List<SiteCategoryEx> siteCategories;

    public static class Base {

        @Getter
        private String userNo;
        @Getter
        private String userName;
        @Getter
        private boolean isPlatformManager;

        @Getter
        @Setter
        private SiteEx platformSite;

        public Base(UcSecurityBasicUser user) {
            this.userNo = user.getUsername();
            this.userName = user.getUserName();
            this.isPlatformManager = user.isPlatformManager();
        }

    }

}
