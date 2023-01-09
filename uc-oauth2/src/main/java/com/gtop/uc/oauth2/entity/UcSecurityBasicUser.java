package com.gtop.uc.oauth2.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author hongzw
 */
@ToString
public class UcSecurityBasicUser extends User {
    private static final long serialVersionUID = 6683433926830697888L;

    public UcSecurityBasicUser(String userNo, String password, boolean enabled, Collection<? extends GrantedAuthority> authorities) {
        super(userNo, password, enabled,true,true,true, authorities);
        this.userNo = userNo;
    }

    public UcSecurityBasicUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    @Getter
    private String userNo;

    @Setter
    @Getter
    private String userName;

    @Setter
    private Boolean platformManager;
    public Boolean isPlatformManager() {
        return platformManager;
    }

    @Setter
    private Boolean mustChangePassword;

    public Boolean isMustChangePassword() {
        return mustChangePassword;
    }

}
