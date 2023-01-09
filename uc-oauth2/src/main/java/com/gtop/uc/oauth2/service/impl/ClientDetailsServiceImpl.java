package com.gtop.uc.oauth2.service.impl;

import com.gtop.uc.oauth2.dao.OauthClientDetailsMapper;
import com.gtop.uc.oauth2.service.IClientDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hongzw
 */
@Service("iClientDetailsService")
public class ClientDetailsServiceImpl implements IClientDetailsService {

    @Resource
    private OauthClientDetailsMapper oauthClientDetailsMapper;

    @Override
    public String getRedirectByClientId(String clientId) {
        return oauthClientDetailsMapper.getWebRedirectByClient(clientId);
    }

    @Override
    public String getRedirectByModuleId(Long id) {
        return oauthClientDetailsMapper.getModuleRedirectByClient(id);
    }
}
