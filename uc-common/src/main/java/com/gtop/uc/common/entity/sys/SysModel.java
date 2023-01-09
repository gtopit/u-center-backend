package com.gtop.uc.common.entity.sys;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hongzw
 */
@Data
public class SysModel implements Serializable {

    private Date createTime;

    private Date updateTime;

    private String createUser;

    private String updateUser;

}
