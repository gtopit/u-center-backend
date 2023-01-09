package com.gtop.uc.admin.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hongzw
 */
public interface ISiteSecurityService {

    /**
     * 获取token
     * @param request
     * @param code
     * @param params
     * @return
     */
    String getToken(HttpServletRequest request, String code, String[] params);

}
