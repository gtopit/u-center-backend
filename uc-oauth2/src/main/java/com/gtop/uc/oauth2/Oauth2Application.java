package com.gtop.uc.oauth2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Locale;

/**
 * @author hongzw
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.gtop.uc")
@MapperScan(basePackages = {"com.gtop.uc.oauth2.dao", "com.gtop.uc.common.cache.dao"})
public class Oauth2Application {
    private static final String STR_ZH = "zh";

    public static void main(String[] args) {
        Locale locale = Locale.getDefault();
        System.out.println("当前语言环境：" + locale.getLanguage());
        if(!locale.getLanguage().startsWith(STR_ZH)){
            Locale.setDefault(Locale.forLanguageTag("zh-CN"));
        }
        System.out.println("最终语言环境：" + Locale.getDefault().getLanguage());
        SpringApplication.run(Oauth2Application.class, args);

    }
}
