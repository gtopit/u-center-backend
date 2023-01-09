package com.gtop.uc.common.entity.sys;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 部门信息Do
 *
 * @author hongzw
 */
@Data
@TableName("sys_department")
public class SysDepartment extends SysModel {

    @TableId
    private Integer id;

    /** 项目ID */
    private Integer siteId;

    /** 父部门编码 */
    private String pno;

    /** 部门编号 */
    private String deptNo;

    /** 部门名称 */
    private String deptName;

    /** 机构全称 */
    private String fullName;

    /** 机构简称 */
    private String shortName;

    /** 全路径 */
    private String fullPath;

    /** 排序时间 */
    private Integer orderTime;

    /** 部门职能 */
    private String deptFunction;

    /** 部门类型(DEPT_TYPE) */
    private Integer deptType;

    /** 联系电话 */
    private String telephone;

    /** 成立日期 */
    private java.util.Date buildDate;

    /** 数据状态（ENABLED） */
    private Integer dataStatus;

    /** JSON字段 */
    private String json;

    /** 版本 */
    private Integer version;

    /** 备注 */
    private String remark;

    /** ID全路径 */
    private String fullIdPath;

    /** 区域编码 */
    private String districtNo;

}
