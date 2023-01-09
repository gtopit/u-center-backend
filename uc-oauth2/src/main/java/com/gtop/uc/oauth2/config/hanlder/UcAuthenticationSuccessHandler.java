package com.gtop.uc.oauth2.config.hanlder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtop.uc.common.constant.GlobalProperties;
import com.gtop.uc.common.response.ApiResponse;
import com.gtop.uc.oauth2.entity.UcSecurityBasicUser;
import com.gtop.uc.oauth2.service.IUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @author hongzw
 */
@Component
public class UcAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	@Resource
	private IUserService userService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {
		Object returnPage = request.getAttribute("returnPage");
		if (null != returnPage && returnPage.equals(true)) {
			redirectStrategy.sendRedirect(request, response, GlobalProperties.AUTHENTICATION_LOGIN_URL);
		}

		response.setContentType("application/json; charset=utf-8");
		ObjectMapper mapper = new ObjectMapper();
		UcSecurityBasicUser user = (UcSecurityBasicUser) authentication.getPrincipal();
		response.getWriter().print(mapper.writeValueAsString(ApiResponse.successWithData(userService.getUserByUserNo(user.getUserNo()))));
	}

}
