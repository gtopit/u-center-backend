package com.gtop.uc.admin.service.impl;

import com.gtop.uc.admin.dao.InterfaceCallLogMapper;
import com.gtop.uc.admin.dao.OauthClientDetailsMapper;
import com.gtop.uc.admin.service.IInterfaceCallLogService;
import com.gtop.uc.common.entity.sys.OauthClientDetails;
import com.gtop.uc.common.entity.sys.SysInterfaceCallLog;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author hongzw
 */
@Service
public class InterfaceCallLogServiceImpl implements IInterfaceCallLogService {

    @Resource
    private OauthClientDetailsMapper oauthClientDetailsMapper;

    @Resource
    private InterfaceCallLogMapper logMapper;

    @Override
    public void saveCallLog(String clientId, String requestUrl) {
        OauthClientDetails details = oauthClientDetailsMapper.selectById(clientId);
        SysInterfaceCallLog log = new SysInterfaceCallLog();
        log.setInterfaceUrl(requestUrl);
        log.setSiteKey(details.getScope());
        log.setCallTime(new Date());
        logMapper.insert(log);
    }

}
