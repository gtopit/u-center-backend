package com.gtop.uc.oauth2.config;

import com.gtop.uc.common.constant.GlobalProperties;
import com.gtop.uc.common.entity.dto.LoginFailDTO;
import com.gtop.uc.common.exception.ApplicationException;
import com.gtop.uc.oauth2.entity.UcSecurityBasicUser;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author hongzw
 * @Description:
 */
public class UcAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    /**
     * The plaintext password used to perform
     * PasswordEncoder#matches(CharSequence, String)}  on when the user is
     * not found to avoid SEC-2056.
     */
    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

    private PasswordEncoder passwordEncoder;

    /**
     * The password used to perform
     * {@link PasswordEncoder#matches(CharSequence, String)} on when the user is
     * not found to avoid SEC-2056. This is necessary, because some
     * {@link PasswordEncoder} implementations will short circuit if the password is not
     * in a valid format.
     */
    private volatile String userNotFoundEncodedPassword;

    private UserDetailsService userDetailsService;

    private UserDetailsPasswordService userDetailsPasswordService;

    // 以userNo为key的用户失败登录测试
    private ConcurrentMap<String, LoginFailDTO> userLoginFailTimesMap = new ConcurrentHashMap<>();

    public UcAuthenticationProvider() {
        setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
    }


    /**
     @Auhtor wly
     @Description: 密码匹配方法重写，可以匹配明文和密文密码
     @Date: Created by 2020/1/7
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            logger.debug("Authentication failed: no credentials provided");

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }

    }


    @Override
    protected void doAfterPropertiesSet() {
        Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");
    }

    @Override
    protected final UserDetails retrieveUser(String username,
                                             UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        prepareTimingAttackProtection();
        try {
            // 判断用户是否被锁定
            if (GlobalProperties.LOGIN_FAIL_LOCK && !checkLock(username)) {
                throw new Exception("该账号已被锁定，请稍后再进行登录操作!");
            }

            UcSecurityBasicUser loadedUser = (UcSecurityBasicUser) this.getUserDetailsService().loadUserByUsername(username);
            if (loadedUser == null) {
                // 增加失败次数
                if (GlobalProperties.LOGIN_FAIL_LOCK) {
                    addFailTimes(username);
                }

                throw new InternalAuthenticationServiceException(
                        "UserDetailsService returned null, which is an interface contract violation");
            }

            String presentedPassword = authentication.getCredentials().toString();
            if (!passwordEncoder.matches(presentedPassword, loadedUser.getPassword())) {
                logger.debug("Authentication failed: password does not match stored value");
                // 增加失败次数
                if (GlobalProperties.LOGIN_FAIL_LOCK) {
                    addFailTimes(username);
                }

                throw new ApplicationException(220, "用户名/密码错误，请重新输入！");
            }

            // 判断是否首次登录未修改密码
            if (GlobalProperties.FIRST_LOGIN_CHANGE_PASSWORD) {
                Boolean mustChangePassword = loadedUser.isMustChangePassword();
                if (mustChangePassword != null && mustChangePassword) {
                    String errorMsg = "该用户当前密码为初始化密码，必须修改密码！";
                    throw new InternalAuthenticationServiceException(errorMsg, new ApplicationException(210, errorMsg));
                }
            }

            return loadedUser;
        }
        catch (UsernameNotFoundException ex) {
            mitigateAgainstTimingAttack(authentication);
            throw ex;
        }
        catch (InternalAuthenticationServiceException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal,
                                                         Authentication authentication, UserDetails user) {
        boolean upgradeEncoding = this.userDetailsPasswordService != null
                && this.passwordEncoder.upgradeEncoding(user.getPassword());
        if (upgradeEncoding) {
            String presentedPassword = authentication.getCredentials().toString();
            String newPassword = this.passwordEncoder.encode(presentedPassword);
            user = this.userDetailsPasswordService.updatePassword(user, newPassword);
        }
        return super.createSuccessAuthentication(principal, authentication, user);
    }

    private void prepareTimingAttackProtection() {
        if (this.userNotFoundEncodedPassword == null) {
            this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
        }
    }

    private void mitigateAgainstTimingAttack(UsernamePasswordAuthenticationToken authentication) {
        if (authentication.getCredentials() != null) {
            String presentedPassword = authentication.getCredentials().toString();
            this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
        }
    }

    /**
     * Sets the PasswordEncoder instance to be used to encode and validate passwords. If
     * not set, the password will be compared using {@link PasswordEncoderFactories#createDelegatingPasswordEncoder()}
     *
     * @param passwordEncoder must be an instance of one of the {@code PasswordEncoder}
     * types.
     */
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
        this.passwordEncoder = passwordEncoder;
        this.userNotFoundEncodedPassword = null;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    protected UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    /**
     * 校验用户登录失败次数
     *
     * @param userNo
     *            用户编号
     * @return 校验用户是否被锁定
     */
    public boolean checkLock(String userNo) {
        LoginFailDTO loginFailDTO = userLoginFailTimesMap.get(userNo);
        if (loginFailDTO == null) {
            return true;
        }

        Date curDate = new Date();

        Integer times = loginFailDTO.getTimes();
        Date lastDate = loginFailDTO.getLastDate();

        long timeDifference = ((curDate.getTime() - lastDate.getTime()) / 60 / 1000);
        if (times >= GlobalProperties.LOGIN_FAIL_MAX_TIMES && timeDifference < GlobalProperties.LOGIN_FAIL_LOCK_MINUTES) {
            return false;
        }
        return true;
    }

    /**
     * 增加用户失败次数
     *
     * @param userNo
     *            用户编号
     */
    private void addFailTimes(String userNo) {
        Date curDate = new Date();

        LoginFailDTO loginFailDTO = userLoginFailTimesMap.get(userNo);
        if (loginFailDTO == null) {
            loginFailDTO = new LoginFailDTO();
            loginFailDTO.setLastDate(curDate);
            loginFailDTO.setTimes(0);
        }

        Integer times = loginFailDTO.getTimes();
        Date lastDate = loginFailDTO.getLastDate();

        long timeDifference = ((curDate.getTime() - lastDate.getTime()) / 60 / 1000);
        // 分钟
        if (timeDifference >= GlobalProperties.LOGIN_FAIL_MAX_TIMES) {
            times = 0;
        }
        times++;
        lastDate = curDate;

        loginFailDTO.setLastDate(lastDate);
        loginFailDTO.setTimes(times);

        userLoginFailTimesMap.put(userNo, loginFailDTO);
    }

}
