package com.gtop.uc.oauth2.service;

/**
 * @author hongzw
 */
public interface IClientDetailsService {

    String getRedirectByClientId(String clientId);

    String getRedirectByModuleId(Long id);

}
