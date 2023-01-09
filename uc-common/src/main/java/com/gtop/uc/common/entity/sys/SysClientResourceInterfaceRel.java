package com.gtop.uc.common.entity.sys;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 应用资源接口关联Do
 *
 * @author hongzw
 */
@Data
@TableName("sys_client_resource_interface_rel")
public class SysClientResourceInterfaceRel extends SysModel {

    @TableId
    private Integer id;

    /** 资源编号 */
    private String resourceId;

    /** 接口ID */
    private Integer interfaceId;

}
