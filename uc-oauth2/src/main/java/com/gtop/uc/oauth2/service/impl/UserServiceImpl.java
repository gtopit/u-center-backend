package com.gtop.uc.oauth2.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.gtop.uc.common.constant.GlobalProperties;
import com.gtop.uc.common.entity.sys.SysUser;
import com.gtop.uc.common.exception.ApplicationException;
import com.gtop.uc.oauth2.dao.SysUserMapper;
import com.gtop.uc.oauth2.entity.SysUserEx;
import com.gtop.uc.oauth2.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hongzw
 */
@Service
public class UserServiceImpl implements IUserService {

	@Resource
	private SysUserMapper sysUserMapper;

	@Override
	public SysUserEx getUserByUserNo(String userNo) {
		SysUser sysUser = new SysUser();
		sysUser.setUserNo(userNo);
		List<SysUserEx> sysUserExes = sysUserMapper.selectUser(sysUser);
		return CollectionUtil.isNotEmpty(sysUserExes) ? sysUserExes.get(0) : null;
	}

	@Override
	public SysUserEx getByUsername(String username) {
		SysUser sysUser = new SysUser();
		sysUser.setUserName(username);
		List<SysUserEx> sysUserExes = sysUserMapper.selectUser(sysUser);
		if (CollectionUtil.isEmpty(sysUserExes) || sysUserExes.size() > 1) {
			throw new ApplicationException(1108, username);
		}
		return sysUserExes.get(0);
	}

	@Override
	public SysUserEx getByTelephone(String telephone) {
		SysUser sysUser = new SysUser();
		sysUser.setUserName(telephone);
		List<SysUserEx> sysUserExes = sysUserMapper.selectUser(sysUser);
		return CollectionUtil.isNotEmpty(sysUserExes) ? sysUserExes.get(0) : null;
	}

	@Override
	public boolean isPlatformSiteManager(String userNo) {
		return sysUserMapper.isSiteManager(userNo, GlobalProperties.PLATFORM_SITE_KEY);
	}

}
