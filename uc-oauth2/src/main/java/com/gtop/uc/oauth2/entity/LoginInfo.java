package com.gtop.uc.oauth2.entity;

import lombok.Data;

/**
 * @author hongzw@citycloud.com.cn
 * @Date 2022/11/17 22:01
 */
@Data
public class LoginInfo {

    private String username;

    private String password;

    private String code;

    private String uuid;

}
