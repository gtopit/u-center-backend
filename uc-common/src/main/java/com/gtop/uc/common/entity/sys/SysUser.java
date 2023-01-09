package com.gtop.uc.common.entity.sys;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户信息Do
 * @author hongzw
 */
@Data
@TableName("sys_user")
public class SysUser extends SysModel {

    @TableId
    private Integer id;

    /** 用户编号 */
    private String userNo;

    /** 用户名称 */
    private String userName;

    /** 身份证号 */
    private String idCard;

    /** 用户所在部门编码 */
    private String deptNo;

    /** 出生年月 */
    private java.util.Date birthday;

    /** 密码 */
    private String password;

    /** 性别 */
    private Integer gender;

    /** 联系电话 */
    private String telephone;

    /** 办公室电话 */
    private String officeTel;

    /** 联系地址 */
    private String address;

    /** 邮箱 */
    private String email;

    /** 年龄 */
    private Integer age;

    /** 界面主题 */
    private String pageTheme;

    /** 权限开始日期 */
    private java.util.Date authStartDate;

    /** 权限结束日期 */
    private java.util.Date authEndDate;

    /** 版本 */
    private Integer version;

    /** 头像路径 */
    private String headerIconPath;

    /** 首次登录是否必须修改密码 */
    private Boolean mustChangePassword;

    /** 备注 */
    private String remark;

    /** 是否具有权限(0-否；1-是) */
    private Boolean enabled;

}
