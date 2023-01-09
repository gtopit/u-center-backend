package com.gtop.uc.common.entity.sys;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 系统应用Do
 *
 * @author hongzw
 */
@Data
@TableName("sys_app")
public class SysApp extends SysModel {

    @TableId
    private Integer id;

    /** 应用名称 */
    private String appName;

    /** 应用类型（SYS_APP_TYPE），0-web应用，1-app应用 */
    private Integer appType;

    /** 应用描述 */
    private String appDescribe;

    /** 应用logo的URL */
    private String appLogo;

    /** 关联的oauth2的应用clientId */
    private String clientId;

}
