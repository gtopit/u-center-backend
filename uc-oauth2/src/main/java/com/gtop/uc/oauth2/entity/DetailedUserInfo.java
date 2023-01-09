package com.gtop.uc.oauth2.entity;


import lombok.Data;

@Data
public class DetailedUserInfo {

    private Long userId;

    private String userNo;

    private String userName;

    private Long deptId;

    private String deptName;

    private Short sex;

    private String ownRoles;

    private Short userGrade;

    private Long currentSiteId;

    private String currentSiteName;

    private Long platformId;

    private Long groupId;

    private String groupNo;

    private String groupName;

    private String fullPath;

}
