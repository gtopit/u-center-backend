package com.gtop.uc.oauth2.config;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.gtop.uc.common.constant.AppConstant;
import com.gtop.uc.oauth2.entity.LoginInfo;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author hongzw
 */
public class UcUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private boolean postOnly = true;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        if(request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)
                || request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)) {

            String username = (String) request.getAttribute(AppConstant.REQ_ATTR_USERNAME);
            String password = (String) request.getAttribute(AppConstant.REQ_ATTR_PASSWORD);
            if (StrUtil.isEmpty(username) && StrUtil.isEmpty(password)) {
                try (InputStream is = request.getInputStream()){
                    LoginInfo loginInfo = JSONUtil.toBean(IoUtil.readUtf8(is), LoginInfo.class);
                    username = loginInfo.getUsername();
                    password = loginInfo.getPassword();
                } catch (IOException e) {
                    username = "";
                    password = "";
                    e.printStackTrace();
                }
            }
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            setDetails(request, authRequest);
            return getAuthenticationManager().authenticate(authRequest);
        }

        else {
            return super.attemptAuthentication(request, response);
        }
    }

}
