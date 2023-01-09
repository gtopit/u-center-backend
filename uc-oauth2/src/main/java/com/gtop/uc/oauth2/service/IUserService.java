package com.gtop.uc.oauth2.service;

import com.gtop.uc.oauth2.entity.SysUserEx;

/**
 * @author hongzw
 */
public interface IUserService {

    SysUserEx getUserByUserNo(String userNo);

    SysUserEx getByUsername(String username);

    SysUserEx getByTelephone(String telephone);

    boolean isPlatformSiteManager(String userNo);

}
