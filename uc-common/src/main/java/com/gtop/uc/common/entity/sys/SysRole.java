package com.gtop.uc.common.entity.sys;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色信息Do
 *
 * @author hongzw
 */
@Data
@TableName("sys_role")
public class SysRole extends SysModel {

    @TableId
    private Integer id;

    /** 站点key */
    private Integer siteKey;

    /** 角色编号 */
    private String roleNo;

    /** 角色名称 */
    private String roleName;

    /** 数据状态（ENABLED） */
    private String enabled;

    /** JSON字段 */
    private String json;

    /** 版本 */
    private Integer version;

    /** 备注 */
    private String remark;

    /** 授予用户 */
    private String grantUsers;

}
