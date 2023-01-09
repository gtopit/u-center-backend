package com.gtop.uc.common.entity.sys;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 站点分类Do
 *
 * @author hongzw
 */
@Data
@TableName("sys_site_category")
public class SysSiteCategory extends SysModel {

    @TableId
    private Integer id;

    /** 站点类型名称 */
    private String cateName;

    /** 描述 */
    private String description;

    /** 图标 */
    private String icon;

    /** 排序时间 */
    private Date orderTime;

}
