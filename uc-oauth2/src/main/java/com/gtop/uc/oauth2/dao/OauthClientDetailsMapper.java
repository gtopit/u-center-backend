package com.gtop.uc.oauth2.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gtop.uc.common.entity.sys.OauthClientDetails;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author hongzw
 */
@Mapper
public interface OauthClientDetailsMapper extends BaseMapper<OauthClientDetails> {

    String getWebRedirectByClient(String clientId);

    String getScopeByClient(String clientId);

    String getModuleRedirectByClient(Long id);

}