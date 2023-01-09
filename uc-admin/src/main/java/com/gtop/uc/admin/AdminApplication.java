package com.gtop.uc.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author hongzw
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.gtop.uc")
@EnableFeignClients("com.gtop.uc.admin.feignclient")
@MapperScan(basePackages = {"com.gtop.uc.admin.dao", "com.gtop.uc.common.cache.dao"})
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}
