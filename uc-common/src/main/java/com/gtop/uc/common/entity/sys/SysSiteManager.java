package com.gtop.uc.common.entity.sys;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 项目（站点）管理员Do
 *
 * @author hongzw
 */
@Data
@TableName("sys_site_manager")
public class SysSiteManager extends SysModel {

    @TableId
    private Integer id;

    /** 项目ID */
    private Integer siteId;

    /** 项目管理员编码 */
    private String userNo;

}
