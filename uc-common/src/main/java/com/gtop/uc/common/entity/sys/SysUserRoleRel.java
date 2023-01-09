package com.gtop.uc.common.entity.sys;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户组信息Do
 *
 * @author hongzw
 */
@Data
@TableName("sys_user_role_rel")
public class SysUserRoleRel extends SysModel {

    @TableId
    private Integer id;

    /** 角色编号 */
    private String roleNo;

    /** 用户编号 */
    private String userNo;

    /** 部门编号 */
    private String deptNo;

}
