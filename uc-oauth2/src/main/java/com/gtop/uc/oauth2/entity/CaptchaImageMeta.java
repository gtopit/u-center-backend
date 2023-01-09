package com.gtop.uc.oauth2.entity;

import lombok.Data;

import java.awt.image.BufferedImage;

/**
 * @author hongzw@citycloud.com.cn
 * @Date 2022/11/18 11:08
 */
@Data
public class CaptchaImageMeta {

    private String imageNumber;

    private BufferedImage image;

}
