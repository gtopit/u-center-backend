package com.gtop.uc.common.entity.sys;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 权限接口调用日志Do
 *
 * @author hongzw
 */
@Data
@TableName("sys_interface_call_log")
public class SysInterfaceCallLog extends SysModel {

    @TableId
    private Integer id;

    /** 站点key */
    private String siteKey;

    /** 接口地址 */
    private String interfaceUrl;

    /** 调用时间 */
    private java.util.Date callTime;

}
