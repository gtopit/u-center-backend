package com.gtop.uc.oauth2.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gtop.uc.common.entity.sys.SysUser;
import com.gtop.uc.oauth2.entity.DetailedUserInfo;
import com.gtop.uc.oauth2.entity.SysUserEx;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hongzw
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 是否站点管理员
     * @param userNo
     * @param siteKey
     * @return
     */
    boolean isSiteManager(@Param(value = "userNo") String userNo, @Param(value = "siteKey") String siteKey);

    DetailedUserInfo getDetailedUserInfoById(DetailedUserInfo user);

    List<SysUserEx> selectUser(SysUser sysUser);

}
