package com.gtop.uc.admin.config;

import cn.hutool.core.date.DateUtil;
import com.gtop.uc.admin.constant.Oauth2Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * @author hongzw
 */
@Slf4j
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {

    public SecurityAuthenticationEntryPoint() {
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.error("Spring Security异常", authException);
        putResponse(response, "token不存在", Oauth2Properties.UC_OAUTH2_SERVICE_USER_LOGOUT_URL, 2404);
    }

    private void putResponse(ServletResponse servletResponse, String errorMsg, String url, long code) throws IOException {
        servletResponse.setCharacterEncoding("utf-8");
        servletResponse.setContentType("application/json;charset=UTF-8");
        OutputStream out = servletResponse.getOutputStream();
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(out, "utf-8"));
        String jsonString = getErrorJsonString(errorMsg, url, code);
        pw.println(jsonString);
        pw.flush();
        pw.close();
    }

    private String getErrorJsonString(String message, String url, Long code) {
        String jsonString = "";
        if (message == null) {
            message = "";
        }

        jsonString = "{\"success\":false,\"code\":" + code + ",\"lastTime\":\"" + DateUtil.now() + "\",\"message\":\"" + message.replace("'", "\\'") + "\",\"url\":\"" + url + "\"}";
        return jsonString;
    }
}
