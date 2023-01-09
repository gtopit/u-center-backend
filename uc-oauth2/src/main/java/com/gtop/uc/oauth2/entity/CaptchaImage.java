package com.gtop.uc.oauth2.entity;

import lombok.Data;

/**
 * 校验码图片
 * @author hongzw@citycloud.com.cn
 * @Date 2022/11/18 10:59
 */
@Data
public class CaptchaImage {

    private String uuid;

    private String img;

}
