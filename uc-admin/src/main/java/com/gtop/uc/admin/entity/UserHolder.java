package com.gtop.uc.admin.entity;

import com.gtop.uc.common.entity.sys.SysDepartment;
import com.gtop.uc.common.entity.sys.SysRole;
import com.gtop.uc.common.entity.sys.SysSite;
import com.gtop.uc.common.entity.sys.SysUser;
import lombok.Data;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.Date;
import java.util.List;

/**
 * @author hongzw
 */
@Data
public class UserHolder {

    private Base base;

    private OAuth2AccessToken accessToken;

    private SysUser user;

    private SysSite site;

    private SysDepartment department;

    private List<SysRole> roles;

    private List<MenuNode> menuNodes;

    @Data
    public static class Base {

        private String ip;

        private Date loginTime;

    }

}
