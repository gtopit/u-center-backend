package com.gtop.uc.common.entity.sys;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 站点菜单Do
 *
 * @author hongzw
 */
@Data
@TableName("sys_site_menu")
public class SysSiteMenu extends SysModel {

    /** 主键id */
    @TableId
    private Integer id;

    /** 站点key */
    private String siteKey;

    /** 功能名称 */
    private String funName;

    /** 功能类型 */
    private Integer funType;

    /** 连接 */
    private String location;

    /** 父功能 */
    private Integer pid;

    /** 排序时间 */
    private java.util.Date orderTime;

    /** 是否记日志（IF） */
    private Integer logFlag;

    /** 图标路径 */
    private String iconPath;

    /** 版本 */
    private Integer version;

    /** 备注 */
    private String remark;

    /** 全路径 */
    private String fullPath;

    /** 帮助页Url */
    private String helpUrl;

    /** 是否以新窗口的方式打开：1-是；0-否 */
    private Boolean isOpenNew;

    /** 是否站点列表显示（IF） */
    private String displayInSitePage;

    /** JSON字段 */
    private String json;

}
