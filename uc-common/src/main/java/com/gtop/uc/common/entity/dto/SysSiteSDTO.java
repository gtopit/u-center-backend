package com.gtop.uc.common.entity.dto;

import com.gtop.uc.common.entity.sys.SysSite;
import lombok.Data;

/**
 * @author hongzw
 */
@Data
public class SysSiteSDTO extends SysSite {

    private String clientSecret;

    private String resourceIds;

}
