package com.gtop.uc.oauth2.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.gtop.uc.common.cache.CachePlatformSiteProperties;
import com.gtop.uc.common.constant.SiteConstant;
import com.gtop.uc.common.response.ApiResponse;
import com.gtop.uc.oauth2.entity.*;
import com.gtop.uc.oauth2.service.IClientDetailsService;
import com.gtop.uc.oauth2.service.ISiteService;
import com.gtop.uc.oauth2.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author hongzw
 */
@Controller
@Slf4j
@RequestMapping("/sessionBased")
public class SessionBasedSiteController {

	@Resource
	private ISiteService siteService;

	@Resource(name = "iClientDetailsService")
	private IClientDetailsService clientDetailsService;

	@Resource(name = "stringRedisTemplate")
	private StringRedisTemplate redisTemplate;

	@Resource
	private IUserService userService;

	/**
	 * 获取用户所在部门编码
	 * @param authentication
	 * @return
	 */
	@GetMapping("/get/deptNo")
	@ResponseBody
	public ApiResponse<String> getDept(Authentication authentication) {
		UcSecurityBasicUser user = (UcSecurityBasicUser) authentication.getPrincipal();
		Assert.notNull(user, "当前用户信息不能为空！");
		String deptNo = redisTemplate.opsForValue().get(String.valueOf(user.getUserNo()));
		if(StringUtils.isEmpty(deptNo)){
			SysUserEx sysUser = userService.getUserByUserNo(user.getUserNo());
			deptNo = sysUser.getDeptNo();
			redisTemplate.opsForValue().set(user.getUserNo(), deptNo);
		}
		return ApiResponse.successWithData(deptNo);
	}

	/**
	 * 缓存中更新用户所在部门编码
	 * @param authentication
	 * @param deptNoMap
	 * @return
	 */
	@PostMapping("/reset/deptNo")
	@ResponseBody
	public ApiResponse<String> changeDept(Authentication authentication, @RequestBody Map<String, String> deptNoMap) {
		UcSecurityBasicUser user = (UcSecurityBasicUser) authentication.getPrincipal();
		Assert.notNull(user, "当前用户信息不能为空！");
		Assert.notNull(deptNoMap, "deptNo不能为空！");
		redisTemplate.opsForValue().set(user.getUserNo(), deptNoMap.get("deptNo"));
		return ApiResponse.successWithMsg(1110);
	}

	/**
	 * 获取用户可以切换的站点列表(手机端)
	 * @param authentication
	 * @return
	 */
	@GetMapping("/get/phoneSiteList")
	@ResponseBody
	public ApiResponse<UserSitesInfo> getAppSiteList(Authentication authentication) {
		UcSecurityBasicUser user = (UcSecurityBasicUser) authentication.getPrincipal();
		Assert.notNull(user, "当前用户信息不能为空！");
		UserSitesInfo userSitesInfo = siteService.getSwitchSiteList(SiteConstant.Type.PHONE, user, null);
		return ApiResponse.successWithMsgAndData(userSitesInfo, 1111);
	}

	/**
	 * 获取用户可以切换的站点列表(PC端)
	 * @param authentication
	 * @param deptNo
	 * @return
	 */
	@GetMapping("/get/pcSiteList")
	@ResponseBody
	public ApiResponse<UserSitesInfo> getSwitchSiteList(
			Authentication authentication,
			@RequestParam(value = "deptNo") String deptNo
	) {
		UcSecurityBasicUser user = (UcSecurityBasicUser) authentication.getPrincipal();
		Assert.notNull(user, "当前用户信息不能为空！");
		UserSitesInfo userSitesInfo = siteService.getSwitchSiteList(SiteConstant.Type.PC, user, deptNo);
		return ApiResponse.successWithMsgAndData(userSitesInfo, 1111);
	}

	@PostMapping("/update/siteSort")
	public @ResponseBody ApiResponse updateSiteSort(@RequestBody SiteSortDTO param) {
		siteService.updateSiteOrder(param);
		return ApiResponse.successWithData("更新用户站点排序成功");
	}

	/** 用户切换站点 */
	@RequestMapping(value = { "/switchSite/{client}/{site}" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public String switchSite(@PathVariable("client") String clientId, @PathVariable("site") String siteId,
							 String prefixUrl, String funId, Long moduleId) {
		String redirectUri = getRedirectUrlBy(clientId, prefixUrl, moduleId);
		String result = "forward:/oauth/authorize?response_type=code&client_id=" + clientId + "&scope=" + siteId;
		if (redirectUri != null) {
			if (StringUtils.isNotBlank(funId)) {
				result += "&redirect_uri=" + redirectUri + "&state=" + redirectUri + "," + funId;
			} else {
				result += "&redirect_uri=" + redirectUri + "&state=" + redirectUri + ",";
			}
		} else {
			if (StringUtils.isNotBlank(funId)) {
				result += "&state=," + funId;
			}
		}
		log.info("站点切换访问：{}", result);
		return result;
	}

	/**
	 * 站点管理员访问站点管理页面
	 * @param siteKey
	 * @param userNo
	 * @param prefixUrl
	 * @return
	 */
	@GetMapping("/switchSiteManager/{siteKey}/{userNo}")
	public String switchSiteManager(@PathVariable("siteKey") String siteKey, @PathVariable("userNo") String userNo,
			String prefixUrl) {
		Assert.notNull(siteKey, "请传入siteKey");
		Assert.notNull(userNo, "请传入userNo");

		String redirectUri = getRedirectUrlBy(CachePlatformSiteProperties.platformSiteClientId, prefixUrl, null);
		String result = "forward:/oauth/authorize?response_type=code&client_id="
				+ CachePlatformSiteProperties.platformSiteClientId + "&scope=" + CachePlatformSiteProperties.platformSiteKey;
		if (redirectUri != null) {
			result += "&redirect_uri=" + redirectUri + "&state=" + redirectUri + "," + siteKey + "," + userNo;
		} else {
			result += "&state=," + siteKey + "," + userNo;
		}
		log.info("站点管理员访问：{}", result);
		return result;
	}

	/** 根据clientID查询回调地址，若来源地址和配置内网地址一致，则返回第一个，反之则返回第二个，不考虑超过2个的情况 */
	private String getRedirectUrlBy(String clientId, String prefixUrl, Long moduleId) {
		if (StringUtils.isEmpty(prefixUrl)) {
			return null;
		}
		String url = null;
		// 子模块跳转时直接取子模块的回调地址
		String redirectStr;
		if (moduleId != null) {
			redirectStr = clientDetailsService.getRedirectByModuleId(moduleId);
		} else {
			redirectStr = clientDetailsService.getRedirectByClientId(clientId);
		}
		if (!StringUtils.isEmpty(redirectStr)) {
			String[] redirectUrls = redirectStr.split(",");
			if (redirectUrls.length == 1) {
				return null;
			} else {
				for (String redirectUrl : redirectUrls) {
					if (redirectUrl.startsWith(prefixUrl)) {
						url = redirectUrl;
						break;
					}
				}
			}
		}
		return url;
	}

	@GetMapping("/loginCheck")
	@ResponseBody
	public ApiResponse<String> loginCheck(Authentication authentication) {
		String username = ((UcSecurityBasicUser) authentication.getPrincipal()).getUserName();
		return ApiResponse.successWithData("欢迎！" + username + "，祝您有美好的一天。 ^-^");
	}

}
