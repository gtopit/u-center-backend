package com.gtop.uc.oauth2.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Description 站点类型对象
 * @author hongzw
 **/
@Data
public class SiteType {

    private String id;

    private String name;

    private String icon;

    private Integer seq;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    private String code;

    private List<SiteInfoForSwitchSite> sites;
}
