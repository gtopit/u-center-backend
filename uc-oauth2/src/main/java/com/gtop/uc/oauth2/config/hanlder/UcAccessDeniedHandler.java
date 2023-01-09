package com.gtop.uc.oauth2.config.hanlder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtop.uc.common.response.ApiResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author hongzw
 */
@Component
public class UcAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json; charset=utf-8");
        ObjectMapper mapper = new ObjectMapper();
        httpServletResponse.getWriter().print(mapper.writeValueAsString(ApiResponse.failedWithMsg(1101)));
    }
}
