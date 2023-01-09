package com.gtop.uc.oauth2.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gtop.uc.common.entity.sys.SysSite;
import com.gtop.uc.oauth2.entity.SiteCategoryEx;
import com.gtop.uc.oauth2.entity.SiteEx;
import com.gtop.uc.oauth2.entity.SiteInfoForSwitchSite;
import com.gtop.uc.oauth2.entity.SiteType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author hongzw
 */
@Mapper
public interface SysSiteMapper extends BaseMapper<SysSite> {

    String selectClientIdBySiteId(@Param("id") long projectId);

    List<SiteEx> listByUserNo(@Param("userNo") String userNo,
                              @Param("appType") Short appType,
                              @Param("deptNo") String deptNo);

    void updateSort(@Param("siteKey") String siteKey, @Param("orderTime") Date orderTime);

    List<SiteCategoryEx> selectSiteCategories();

    List<Map<String, Object>> selectDisplayFunctionList(@Param("projectId") Long projectId);

    List<Map<String, Object>> selectProjectModuleList(@Param("projectId") Long projectId);

}
