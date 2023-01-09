package com.gtop.uc.oauth2.service.impl;

import com.gtop.uc.common.constant.UserAuthorityConstant;
import com.gtop.uc.common.entity.sys.SysUser;
import com.gtop.uc.oauth2.entity.UcSecurityBasicUser;
import com.gtop.uc.oauth2.service.IUserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义用户认证与授权
 * <p>
 * Description:
 * </p>
 *
 * @author hongzw
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userService.getUserByUserNo(username);

        // 手机号登录
        if (user == null) {
            user = userService.getByTelephone(username);
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (user != null) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(UserAuthorityConstant.Authority.ADMIN);
            grantedAuthorities.add(grantedAuthority);
            if(UserAuthorityConstant.UserNo.ADMIN.equals(user.getUserNo())){
                grantedAuthority = new SimpleGrantedAuthority(UserAuthorityConstant.Authority.ROLE_SUPER_USER);
                grantedAuthorities.add(grantedAuthority);
            }

            UcSecurityBasicUser basicUser = new UcSecurityBasicUser(user.getUserNo(), user.getPassword(), user.getEnabled(), grantedAuthorities);
            basicUser.setUserName(user.getUserName());
            // 判断是否为平台站点管理员
            basicUser.setPlatformManager(userService.isPlatformSiteManager(user.getUserNo()));
            basicUser.setMustChangePassword(user.getMustChangePassword());
            return basicUser;
        } else {
            throw new UsernameNotFoundException("username " + username + " not found");
        }
    }
}
