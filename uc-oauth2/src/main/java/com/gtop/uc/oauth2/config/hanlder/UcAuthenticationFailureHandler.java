package com.gtop.uc.oauth2.config.hanlder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtop.uc.common.exception.ApplicationException;
import com.gtop.uc.common.exception.ValidateCodeException;
import com.gtop.uc.common.response.ApiResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author hongzw
 */
@Component
public class UcAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

        response.setContentType("application/json; charset=utf-8");
        ObjectMapper mapper = new ObjectMapper();

        if (e instanceof BadCredentialsException) {
            response.getWriter().print(mapper.writeValueAsString(ApiResponse.failedWithMsg(1100)));
        } else if (e instanceof UsernameNotFoundException) {
            response.getWriter().print(mapper.writeValueAsString(ApiResponse.failedWithMsg(1100)));
        } else if (e instanceof InternalAuthenticationServiceException || e instanceof ValidateCodeException) {
            Throwable throwable = e.getCause();
            if (throwable instanceof ApplicationException) {
                ApplicationException exception = (ApplicationException) throwable;
                response.getWriter().print(mapper.writeValueAsString(ApiResponse.failedWithMsg(1106, exception.getErrorMessage())));
            } else {
                response.getWriter().print(mapper.writeValueAsString(ApiResponse.failedWithMsg(1106, e.getMessage())));            }
        }  else if (e instanceof DisabledException) {
            response.getWriter().print(mapper.writeValueAsString(ApiResponse.failedWithMsg(1101)));
        } else {
            response.getWriter().print(mapper.writeValueAsString(ApiResponse.failedWithMsg(1107)));
        }
    }
}
