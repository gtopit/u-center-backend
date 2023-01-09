package com.gtop.uc.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gtop.uc.admin.entity.MenuNode;
import com.gtop.uc.common.entity.sys.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hongzw
 */
@Mapper
public interface SiteSecurityMapper extends BaseMapper<SysUser> {

    /**
     * 是否站点管理员
     * @param userNo
     * @param siteKey
     * @return
     */
    boolean isSiteManager(@Param(value = "userNo") String userNo, @Param(value = "siteKey") String siteKey);

    /**
     * 根据站点key获取站点信息
     * @param siteKey 站点key
     * @return
     */
    SysSite selectSiteBySiteKey(@Param(value = "siteKey") String siteKey);

    /**
     * 根据用户编号获取部门信息
     * @param userNo 用户编号
     * @return
     */
    SysDepartment selectDepartmentByUserNo(@Param(value = "userNo") String userNo);

    /**
     * 根据用户编号获取角色信息
     * @param userNo 用户编号
     * @return
     */
    List<SysRole> selectRoleByUserNo(@Param(value = "userNo") String userNo);

    /**
     * 根据站点key获取站点功能菜单
     * @param siteKey 站点key
     * @return
     */
    List<MenuNode> selectMenuBySiteKey(@Param(value = "siteKey") String siteKey);

}
