package com.gtop.uc.oauth2.config;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.gtop.uc.common.cache.CacheService;
import com.gtop.uc.common.constant.AppConstant;
import com.gtop.uc.common.exception.ValidateCodeException;
import com.gtop.uc.oauth2.entity.LoginInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author hongzw
 */
@Slf4j
public class ValidateCodeFilter extends OncePerRequestFilter {

    private static final String LOGIN_PATH = "/login";

    private AuthenticationFailureHandler failureHandler;

    private boolean postOnly = true;

    public void setFailureHandler(AuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("进入验证码过滤器：请求uri - {}, servletPath - {}", request.getRequestURI(), request.getServletPath());

        // 非登陆请求不校验验证码
        if (!LOGIN_PATH.equals(request.getServletPath())) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            doValidateCode(request);
            filterChain.doFilter(request, response);
        } catch (ValidateCodeException e) {
            failureHandler.onAuthenticationFailure(request, response, e);
        }
    }

    private void doValidateCode(HttpServletRequest request) {

        String randomCode = null;
        String uuid = null;
        if(request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)
                || request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)) {

            try (InputStream is = request.getInputStream()) {
                LoginInfo loginInfo = JSONUtil.toBean(IoUtil.readUtf8(is), LoginInfo.class);
                randomCode = loginInfo.getCode();
                uuid = loginInfo.getUuid();
                // 由于后续UcUsernamePasswordAuthenticationFilter过滤器需要使用到username和password信息
                // 而使用IoUtil.readUtf8(is)已经读取了request的流并关闭，因此需要将username和password信息保存
                request.setAttribute(AppConstant.REQ_ATTR_USERNAME, loginInfo.getUsername());
                request.setAttribute(AppConstant.REQ_ATTR_PASSWORD, loginInfo.getPassword());
            } catch (IOException e) {
                throw new ValidateCodeException("校验码不符合规则");
            }

        }

        CacheService cacheService = SpringUtil.getBean(CacheService.class);

        String cacheCode = (String) cacheService.get(AppConstant.CACHE_CAPTCHA_KEY_PREFIX + uuid);

        if (StrUtil.isNotBlank(cacheCode)) {
            // 随手清除 缓存 中验证码，无论验证成功还是失败
            cacheService.remove(AppConstant.CACHE_CAPTCHA_KEY_PREFIX + uuid);
        }
        // 校验不通过，抛出异常
        if (StrUtil.isBlank(randomCode)) {
            throw new ValidateCodeException("请输入验证码！");
        }
        if (StrUtil.isBlank(cacheCode)) {
            throw new ValidateCodeException("验证码已失效，请刷新！");
        }
        if (!randomCode.equals(cacheCode)) {
            throw new ValidateCodeException("验证码错误！");
        }
    }

}
