package com.gtop.uc.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gtop.uc.common.entity.sys.SysAdminInterface;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hongzw
 */
@Mapper
public interface AdminInterfaceMapper extends BaseMapper<SysAdminInterface> {

    List<String> selectInterfaceUrlBy(@Param("list") List<String> resourceIds);

}
