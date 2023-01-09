package com.gtop.uc.oauth2.entity;

import com.gtop.uc.common.entity.sys.SysSiteCategory;
import lombok.Data;

import java.util.List;

/**
 * @author hongzw
 */
@Data
public class SiteCategoryEx extends SysSiteCategory {

    private List<SiteEx> sites;

}
