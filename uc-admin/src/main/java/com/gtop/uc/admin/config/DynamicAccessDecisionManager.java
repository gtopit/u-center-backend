package com.gtop.uc.admin.config;

import com.gtop.uc.admin.constant.AdminConstant;
import com.gtop.uc.admin.service.IAdminInterfaceService;
import com.gtop.uc.admin.service.IInterfaceCallLogService;
import com.gtop.uc.common.cache.CachePlatformSiteProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.AntPathMatcher;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author hongzw
 */
@Slf4j
public class DynamicAccessDecisionManager implements AccessDecisionManager {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Resource
    private IAdminInterfaceService adminInterfaceService;

    @Resource
    private IInterfaceCallLogService interfaceCallLogService;

    private List<String> excludePathPatternList = new ArrayList<>();

    @PostConstruct
    public void init() {
        excludePathPatternList.add("/js/CodeData.js");
        excludePathPatternList.add("/js/CodeConfigData.js");

        excludePathPatternList.add("/getTokenByCode");
    }

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {

        FilterInvocation filterInvocation = (FilterInvocation) object;
        HttpServletRequest request = filterInvocation.getRequest();

        String requestMethod = request.getMethod();
        String requestUrl = request.getServletPath();
        log.info("OAuth2动态权限过滤：请求方式 -- {}，请求路径 -- {}", requestMethod, requestUrl);

        // 不过滤的请求直接放行
        for (String pathPattern : excludePathPatternList) {
            if (antPathMatcher.match(pathPattern, requestUrl)) {
                log.info("OAuth2动态权限过滤：忽略请求 -- {}，匹配规则 -- {}", requestUrl, pathPattern);
                return;
            }
        }

        // 获取client_id（两种方式：验签和token）
        // 验签方式直接从传参中获取，此时若无权限即使抛出AccessDeniedException异常，也无法进入AccessDeniedHandler中处理
        // 验签权限校验不通过属于OAuth2未登录的异常，此时进入ExceptionTranslationFilter处理，只有通过token形式校验失败才会进入AccessDeniedHandler
        // 验签方式直接从传参中获取

        // 匿名访问当作验签方式请求，直接释放交由ClientFilter处理
        if (authentication instanceof AnonymousAuthenticationToken) {
            return;
        }
        // 解析token，获取clientId
        String clientId = ((OAuth2Authentication) authentication).getOAuth2Request().getClientId();

        // 请求来自权限应用时也不过滤
        if (CachePlatformSiteProperties.platformSiteClientId.equalsIgnoreCase(clientId)) {
            return;
        }

        log.info("OAuth2动态权限过滤：请求应用 -- {}", clientId);

        if (adminInterfaceService.check(clientId, requestUrl)) {
            // 记录站点接口调用日志
            interfaceCallLogService.saveCallLog(clientId, requestUrl);
            return;
        }
        throw new AccessDeniedException(AdminConstant.ACCESS_DENIED_MSG);
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}