package com.gtop.uc.admin.entity;

import com.gtop.uc.common.entity.sys.SysSiteMenu;
import lombok.Data;

import java.util.List;

/**
 * @author hongzw
 */
@Data
public class MenuNode extends SysSiteMenu {

    private List<MenuNode> children;

}
