package com.gtop.uc.admin.config;

import com.gtop.uc.admin.filter.RequestParameterFilter;
import com.gtop.uc.common.cache.CachePlatformSiteProperties;
import com.gtop.uc.common.exception.Auth2ResponseExceptionTranslator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.header.HeaderWriterFilter;


/**
 * @author 51117
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {


    final UcAccessDeniedHandler accessDeniedHandler;

    public ResourceServerConfiguration(final UcAccessDeniedHandler accessDeniedHandler) {

        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);

        http.addFilterAfter(new RequestParameterFilter(), HeaderWriterFilter.class);

        http.csrf().disable()//.authorizeRequests()
                .exceptionHandling()
                // 定义的不存在access_token时候响应
                .authenticationEntryPoint(new SecurityAuthenticationEntryPoint())
                .and()
                .authorizeRequests()
                .antMatchers("/v1/security/getCurrentUserInfo").authenticated()
                .anyRequest().permitAll()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(
                            O fsi) {
                        fsi.setAccessDecisionManager(dynamicAccessDecisionManager());
                        return fsi;
                    }
                });
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(CachePlatformSiteProperties.resourceIds).stateless(true);
        // 定义异常转换类生效
        AuthenticationEntryPoint authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
        ((OAuth2AuthenticationEntryPoint) authenticationEntryPoint).setExceptionTranslator(new Auth2ResponseExceptionTranslator());
        resources.authenticationEntryPoint(authenticationEntryPoint);
    }

    @Bean
    public AccessDecisionManager dynamicAccessDecisionManager() {
        return new DynamicAccessDecisionManager();
    }
}
