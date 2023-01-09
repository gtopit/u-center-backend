package com.gtop.uc.common.cache.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gtop.uc.common.entity.dto.SysSiteSDTO;
import com.gtop.uc.common.entity.sys.SysSite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author hongzw
 */
@Mapper
public interface SiteMapper extends BaseMapper<SysSite> {

    @Select("select a.client_id as clientId, b.secret as clientSecret, b.resource_ids as resourceIds " +
            " from sys_site a inner join oauth_client_details b on a.client_id = b.client_id where a.site_key = #{siteKey}")
    SysSiteSDTO selectSiteBySiteKey(@Param("siteKey") String siteKey);

}
