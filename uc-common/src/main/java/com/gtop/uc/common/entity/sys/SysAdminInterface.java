package com.gtop.uc.common.entity.sys;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 应用功能Do
 *
 * @author hongzw
 */
@Data
@TableName("sys_admin_interface")
public class SysAdminInterface {

    @TableId
    private Integer id;

    /** 接口名称 */
    private String interfaceName;

    /** 接口地址 */
    private String interfaceUrl;

    /** 接口类型（预留） */
    private Integer interfaceType;

    /** 排序号 */
    private java.util.Date orderTime;

    /** 是否记日志（IF） */
    private Integer logFlag;

    /** 版本 */
    private Integer version;

    /** 备注 */
    private String remark;

    /** 创建人 */
    private String createUser;

    /** 更新人 */
    private String updateUser;

    /** 创建时间 */
    private java.util.Date createTime;

    /** 更新时间 */
    private java.util.Date updateTime;


}
