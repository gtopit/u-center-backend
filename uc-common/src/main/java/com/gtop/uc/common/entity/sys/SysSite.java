package com.gtop.uc.common.entity.sys;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 站点信息Do
 *
 * @author hongzw
 */
@Data
@TableName("sys_site")
public class SysSite extends SysModel {

    @TableId
    private Integer id;

    /** 站点I编号 */
    private String siteKey;

    /** 分类ID(sys_site_category表id) */
    private Integer cateId;

    /** 站点名称 */
    private String siteName;

    /** 站点管理员编号 */
    private String managerNo;

    /** 站点管理员名称 */
    private String managerName;

    /** 所有站点I管理员编号，多个用逗号隔开 */
    private String managerNos;

    /** 所有站点I管理员名称，多个用逗号隔开 */
    private String managerNames;

    /** 图标路径 */
    private String iconPath;

    /** 背景路径 */
    private String backgroundPath;

    /** 数据状态（0-否；1-是） */
    private String enabled;

    /** 版本 */
    private Integer version;

    /** 数据组，有逗号隔开的数据组名称 */
    private String dataGroups;

    /** 备注 */
    private String remark;

    /** 排序时间 */
    private Date orderTime;

    /** MQ开关，0:关，1:开 */
    private Boolean isMqEnabled;

    /** 关联的oauth2的应用clientId */
    private String clientId;

}
