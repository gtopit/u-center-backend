package com.gtop.uc.oauth2.config;

import com.gtop.uc.common.constant.GlobalProperties;
import com.gtop.uc.oauth2.config.hanlder.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author hongzw@citycloud.com.cn
 * @Date 2022/10/13 15:51
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UcAuthenticationEntryPoint authenticationEntryPoint;
    private final UcAccessDeniedHandler accessDeniedHandler;
    private final UcAuthenticationSuccessHandler successHandler;
    private final UcAuthenticationFailureHandler failureHandler;
    private final UserDetailsService userDetailsService;
    private final UcLogoutSuccessHandler logoutSuccessHandler;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public WebSecurityConfiguration(
            UcAuthenticationEntryPoint authenticationEntryPoint,
            UcAccessDeniedHandler accessDeniedHandler,
            UcAuthenticationSuccessHandler successHandler,
            UcAuthenticationFailureHandler failureHandler,
            UserDetailsService userDetailsService,
            UcLogoutSuccessHandler logoutSuccessHandler,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.userDetailsService = userDetailsService;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint);

        http.csrf().disable();
        // 允许iframe嵌套
        http.headers().frameOptions().disable();

        //登陆配置
        http.formLogin().loginProcessingUrl("/login");

        if (GlobalProperties.NEED_CAPTCHA) {
            ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
            validateCodeFilter.setFailureHandler(failureHandler);
            // 将图片验证码过滤器添加在 UsernamePasswordAuthenticationFilter 之前
            http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class);
        }
        UcUsernamePasswordAuthenticationFilter authenticationFilter = new UcUsernamePasswordAuthenticationFilter();
        authenticationFilter.setAuthenticationSuccessHandler(successHandler);
        authenticationFilter.setAuthenticationFailureHandler(failureHandler);
        authenticationFilter.setAuthenticationManager(authenticationManagerBean());
        http.addFilterAt(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.logout()
                .logoutSuccessHandler(logoutSuccessHandler)
                .deleteCookies("JSESSIONID");

        http.authorizeRequests().anyRequest()/*所有请求*/.authenticated();/*都需要认证*/
    }

    @Override
    public void configure(WebSecurity web) {

        web.ignoring().antMatchers("/oauth/check_token", "/static/**");

        web.ignoring().antMatchers("/user/getUserInfo", "/nonSessionBased/**", "/captchaImage/**");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        UcAuthenticationProvider provider = new UcAuthenticationProvider();
        provider.setHideUserNotFoundExceptions(false);
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        auth.authenticationProvider(provider);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
