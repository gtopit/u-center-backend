package com.gtop.uc.oauth2.config.hanlder;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtop.uc.common.constant.GlobalProperties;
import com.gtop.uc.common.response.ApiResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hongzw
 */
@Component
public class UcAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        Map<String, String> data = new HashMap<>(4);
        data.put("loginPage", GlobalProperties.AUTHENTICATION_LOGIN_URL);
        data.put("loginUrl", GlobalProperties.AUTHENTICATION_API_URL + "/login");
        ApiResponse result = ApiResponse.failedWithMsg(1104);
        result.setData(data);
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print(mapper.writeValueAsString(result));
    }
}
