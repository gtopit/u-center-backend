package com.gtop.uc.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.server.HttpServerRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gtop.uc.admin.constant.AdminConstant;
import com.gtop.uc.admin.dao.SiteSecurityMapper;
import com.gtop.uc.admin.entity.MenuNode;
import com.gtop.uc.admin.entity.UserHolder;
import com.gtop.uc.admin.feignclient.Oauth2ServiceFeign;
import com.gtop.uc.admin.service.ISiteSecurityService;
import com.gtop.uc.common.cache.CachePlatformSiteProperties;
import com.gtop.uc.common.constant.GlobalProperties;
import com.gtop.uc.common.constant.SessionConstant;
import com.gtop.uc.common.entity.sys.*;
import com.gtop.uc.common.message.MessageHelper;
import com.gtop.uc.common.utils.HttpServletRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author hongzw
 */
@Slf4j
@Service
public class SiteSecurityServiceImpl implements ISiteSecurityService {

    private static final String HTTP = "http://";

    private static final String HTTPS = "https://";

    private static final String SEPARATOR = ",";

    @Resource
    private Oauth2ServiceFeign oauth2Service;

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    @Value("${spring.session.timeout:" + AdminConstant.DEFAULT_SESSION_TIMEOUT + "}")
    private long sessionTimeout;

    @Resource
    private SiteSecurityMapper siteSecurityMapper;

    @Override
    public String getToken(HttpServletRequest request, String code, String[] params) {

        // 配置多个回调地址需要添加回调地址,回调地址需与请求code一致
        String redirectUrl = null;

        if (StrUtil.isNotEmpty(params[0])) {
            redirectUrl = params[0];
        }

        log.debug("code={},clientId={},clientSecret={},redirectUrl={}", code, CachePlatformSiteProperties.platformSiteClientId, CachePlatformSiteProperties.platformSiteClientSecret, redirectUrl);
        DefaultOAuth2AccessToken tokenInfo = oauth2Service.getToken("authorization_code", code, CachePlatformSiteProperties.platformSiteClientId, CachePlatformSiteProperties.platformSiteClientSecret, redirectUrl);
        log.debug("tokenInfo={}", tokenInfo);

        String scope = null;
        if (StringUtils.isEmpty(params[1])) {
            Iterator<String> iterator = tokenInfo.getScope().iterator();
            if (iterator.hasNext()) {
                scope = iterator.next();
                log.debug("scope={}", scope);
            }
        } else {
            scope = params[1];
        }
        String siteKey = scope;
        String userNo = params[2];
        log.debug("userNo={}", userNo);

        // frontUrl配置为相对路径,获取token后回调的地址通过回调地址给的url前缀来匹配
        // 截取从第8位开始，是去除了http://和https://，因为http://后必须有至少一个的字符，所以8应该没问题
        // 相对地址
        String respRedirectUrl = GlobalProperties.PLATFORM_SITE_DASHBOARD_URL;
        // 若回调的地址为空，则无法获取对应回调地址，则userReal无效，只能使用绝对前端地址
        if (StrUtil.isNotEmpty(redirectUrl)) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(redirectUrl);
            URI uri = builder.build(true).toUri();
            String[] respRedirectUrls = respRedirectUrl.split(SEPARATOR);
            for (String url : respRedirectUrls) {
                if (url.contains(uri.getHost())) {
                    respRedirectUrl = url;
                    break;
                }
            }
        }
        // 从接口入参state中获取的redirectUrl为null时，配置不当会存在下面的情况
        if (respRedirectUrl.contains(SEPARATOR)) {
            respRedirectUrl = respRedirectUrl.split(SEPARATOR)[0];
        }

        // 地址未配置协议时，默认http
        if (!respRedirectUrl.startsWith(HTTP) && !respRedirectUrl.startsWith(HTTPS)) {
            respRedirectUrl = HTTP + respRedirectUrl;
        }

        userLoginInit(request, tokenInfo, siteKey, userNo);

        String realRedirect = "redirect:" + respRedirectUrl + "?access_token=" + tokenInfo.getValue() + "&scope="
                + scope;

        log.info("权限服务获取token后重定向地址为：realRedirect={}", realRedirect);

        return realRedirect;

    }

    private void userLoginInit(HttpServletRequest request, OAuth2AccessToken accessToken, String siteKey, String userNo) {
        // 判断是否有站点管理员角色
        checkPrivilege(siteKey, userNo);
        UserHolder userHolder = new UserHolder();

        // 设置基础信息
        populateBase(userHolder, request);
        // 设置accessToken信息
        populateAccessToken(userHolder, accessToken);

        // 判断是否为平台站点
        if (isPlatformSite(siteKey)) {

            SitePlatformUserLoginInit(userHolder, userNo, siteKey);

        } else {
            // 一般站点
            commonUserLoginInit(userHolder, siteKey, userNo);

        }

        // 缓存token信息（与session生命周期一致）
        cacheToken(request, accessToken, siteKey);
    }

    private void cacheToken(HttpServletRequest request, OAuth2AccessToken accessToken, String siteKey) {
        String sessionId = request.getSession().getId();
        redisTemplate.opsForValue().set(SessionConstant.SESSION_NAMESPACE + accessToken.getValue() + ":" + siteKey, sessionId, sessionTimeout, TimeUnit.SECONDS);
    }

    private void commonUserLoginInit(UserHolder userHolder, String siteKey, String userNo) {

        populateUser(userHolder, userNo);

        populateSite(userHolder, siteKey);

        populateDepartment(userHolder);

        populateRoles(userHolder);

        populateSiteFunctionNodes(userHolder, siteKey);

    }

    private void SitePlatformUserLoginInit(UserHolder userHolder, String userNo, String siteKey) {

        populateUser(userHolder, userNo);

        populateSite(userHolder, siteKey);

        populateDepartment(userHolder);

    }

    private void populateSiteFunctionNodes(UserHolder userHolder, String siteKey) {
        List<MenuNode> menuList = siteSecurityMapper.selectMenuBySiteKey(siteKey);
        List<MenuNode> nodeList = treeifyFunctionList(menuList);
        userHolder.setMenuNodes(nodeList);
    }

    private List<MenuNode> treeifyFunctionList(List<MenuNode> menuList) {
        List<MenuNode> nodeList = new ArrayList<>();

        if (CollectionUtil.isEmpty(menuList)) {
            return new ArrayList<>();
        }

        Map<Integer, MenuNode> increaseMap = new HashMap<>(menuList.size());

        // 借助increaseMap，遍历完整个链表，即可得到具有父子级目录结构的链表，时间复杂度O(n)
        menuList.forEach(e -> {
            increaseMap.put(e.getId(), e);
            if (e.getPid() != null) {
                List<MenuNode> children = increaseMap.get(e.getPid()).getChildren();
                if (children == null) {
                    children = new ArrayList<>();
                    increaseMap.get(e.getPid()).setChildren(children);
                }
                children.add(e);
            }
        });

        // 取出根节点
        increaseMap.values().forEach(e -> {
            if (e.getPid() == null) {
                nodeList.add(e);
            }
        });

        return nodeList;
    }

    private void populateRoles(UserHolder userHolder) {
        List<SysRole> roles = siteSecurityMapper.selectRoleByUserNo(userHolder.getUser().getUserNo());
        userHolder.setRoles(roles);
    }

    private void populateDepartment(UserHolder userHolder) {
        SysDepartment department = siteSecurityMapper.selectDepartmentByUserNo(userHolder.getUser().getUserNo());
        userHolder.setDepartment(department);
    }

    private void populateSite(UserHolder userHolder, String siteKey) {
        SysSite site = siteSecurityMapper.selectSiteBySiteKey(siteKey);
        userHolder.setSite(site);
    }

    private void populateUser(UserHolder userHolder, String userNo) {
        QueryWrapper<SysUser> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_no", userNo);
        SysUser user = siteSecurityMapper.selectOne(userQueryWrapper);
        userHolder.setUser(user);
    }

    private void populateAccessToken(UserHolder userHolder, OAuth2AccessToken accessToken) {
        userHolder.setAccessToken(accessToken);
    }

    private void populateBase(UserHolder userHolder, HttpServletRequest request) {
        UserHolder.Base base = new UserHolder.Base();
        base.setIp(HttpServletRequestUtil.getIpAddress(request));
        base.setLoginTime(new Date());
        userHolder.setBase(base);
    }

    private boolean isPlatformSite(String siteKey) {

        return CachePlatformSiteProperties.platformSiteKey.equals(siteKey);

    }

    private void checkPrivilege(String siteKey, String userNo) {
        boolean isSiteManager = siteSecurityMapper.isSiteManager(userNo, siteKey);
        Assert.isTrue(isSiteManager, MessageHelper.getApplicationMessage(2100));
    }

}
