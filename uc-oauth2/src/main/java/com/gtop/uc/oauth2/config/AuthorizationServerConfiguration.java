package com.gtop.uc.oauth2.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtop.uc.common.constant.GlobalProperties;
import com.gtop.uc.common.exception.Auth2ResponseExceptionTranslator;
import com.gtop.uc.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hongzw
 */
@Configuration
@EnableAuthorizationServer
@Slf4j
@Lazy
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter
		implements ApplicationListener<ContextRefreshedEvent>{

	/** 注入 authenticationManager 来支持 OAuth2 的密码授权模式（password grant type） */
	private final AuthenticationManager authenticationManager;
	private final UserDetailsService userDetailsService;
	private final DataSource dataSource;

	@Resource
	private AuthorizationServerSecurityConfiguration authorizationServerSecurityConfiguration;

	private UcClientCredentialsTokenEndpointFilter clientCredentialsTokenEndpointFilter;

	private HttpSecurity httpSecurity;

	private CompositeObjectPostProcessor objectPostProcessor = new CompositeObjectPostProcessor();

	@Resource
	private RedisTemplate redisTemplate;

	@Resource
	public RedisConnectionFactory redisConnectionFactory;

	@Autowired
	public AuthorizationServerConfiguration(AuthenticationManager authenticationManager,
                                            UserDetailsService userDetailsService, DataSource dataSource) {
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.dataSource = dataSource;
	}

	@Bean
	public TokenStore tokenStore() {
		// 基于 JDBC 实现，令牌保存到数据
//		return new JdbcTokenStore(dataSource);

		// 使用redis存储token
		UcRedisTokenStore redisTokenStore = new UcRedisTokenStore(redisConnectionFactory);
		//设置redis token存储中的前缀
		redisTokenStore.setPrefix("auth2-token:");
		return redisTokenStore;
	}

	@Bean
	public ClientDetailsService jdbcClientDetails() {
		// 基于 JDBC 实现，需要事先在数据库配置客户端信息
		return new JdbcClientDetailsService(dataSource);
	}

	/**
	 * 配置 authenticationManager 来支持 OAuth2 的密码授权模式（password grant type）
	 *
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

		endpoints.tokenStore(tokenStore())
				.tokenServices(tokenServices())
				.authorizationCodeServices(inRedisAuthorizationCodeServices(redisTemplate))
				.authenticationManager(authenticationManager)
				.exceptionTranslator(new Auth2ResponseExceptionTranslator())
				.userDetailsService(userDetailsService)
				.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST).reuseRefreshTokens(true)
				// 自定义令牌检查端点
		.pathMapping("/oauth/introspect", "/oauth/check_token");
	}

	@Bean
	public AuthorizationCodeServices inRedisAuthorizationCodeServices(RedisTemplate redisTemplate) {
		return new RedisAuthorizationCodeServices(redisTemplate);
	}

	@Bean
	public AuthorizationServerTokenServices tokenServices() {
		UcTokenServices tokenServices = new UcTokenServices();
		tokenServices.setAccessTokenValiditySeconds(GlobalProperties.OAUTH2_ACCESS_TOKEN_TIMEOUT);
		tokenServices.setRefreshTokenValiditySeconds(GlobalProperties.OAUTH2_REFRESH_TOKEN_TIMEOUT);
		tokenServices.setSupportRefreshToken(true);
		tokenServices.setTokenStore(tokenStore());
		return tokenServices;
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.withClientDetails(jdbcClientDetails());
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) {
		security.addTokenEndpointAuthenticationFilter(myClientCredentialsTokenEndpointFilter());
	}

	/**
	 * 自定义.allowFormAuthenticationForClients实现
	 */
	private UcClientCredentialsTokenEndpointFilter myClientCredentialsTokenEndpointFilter() {
		UcClientCredentialsTokenEndpointFilter clientCredentialsTokenEndpointFilter = new UcClientCredentialsTokenEndpointFilter();
		OAuth2AuthenticationEntryPoint authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
		authenticationEntryPoint.setTypeName("Form");
		authenticationEntryPoint.setRealmName("oauth2/client");
		clientCredentialsTokenEndpointFilter.setAuthenticationFailureHandler((request, response, exception) -> {
			response.setContentType("application/json; charset=utf-8");
			ObjectMapper mapper = new ObjectMapper();
			String errorMsg = "ClientId/Secret错误，请仔细核对！";
			response.getWriter().print(mapper.writeValueAsString(
					ApiResponse.failedWithMsg(1112)));
		});
		clientCredentialsTokenEndpointFilter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				// no-op - just allow filter chain to continue to token endpoint
			}
		});
		Field field = null;
		// 通过反射拿到AuthorizationServerSecurityConfiguration中的httpSecurity 先保存起来，因为还没有加载完成，
		try {
			field = WebSecurityConfigurerAdapter.class.getDeclaredField("http");
			field.setAccessible(true);
			this.httpSecurity = (HttpSecurity) field
					.get(authorizationServerSecurityConfiguration);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 自动注入的authenticationManager和允许客户端form生成的不一致，故此处不设置，等Spring启动完成，
		// 则通过httpSecurity进行设置
		clientCredentialsTokenEndpointFilter.setAuthenticationEntryPoint(authenticationEntryPoint);
		clientCredentialsTokenEndpointFilter = postProcess(clientCredentialsTokenEndpointFilter);
		// 将自定义的允许客户端通过账号密码form访问类保存
		this.clientCredentialsTokenEndpointFilter = clientCredentialsTokenEndpointFilter;
		return clientCredentialsTokenEndpointFilter;
	}

	protected <T> T postProcess(T object) {
		return (T) this.objectPostProcessor.postProcess(object);
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		log.info("TC_OAUTH2加载完成");
		// 项目加载完成确保httpSecurity中的配置都加载完成，则获取AuthenticationManager，并设置
		this.clientCredentialsTokenEndpointFilter
				.setAuthenticationManager(this.httpSecurity.getSharedObject(AuthenticationManager.class));
	}

	private static final class CompositeObjectPostProcessor implements ObjectPostProcessor<Object> {
		private List<ObjectPostProcessor<?>> postProcessors = new ArrayList<>();

		@Override
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Object postProcess(Object object) {
			for (ObjectPostProcessor opp : postProcessors) {
				Class<?> oppClass = opp.getClass();
				Class<?> oppType = GenericTypeResolver.resolveTypeArgument(oppClass, ObjectPostProcessor.class);
				if (oppType == null || oppType.isAssignableFrom(object.getClass())) {
					object = opp.postProcess(object);
				}
			}
			return object;
		}

	}
}
