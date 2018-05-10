package com.project.service.business;

import com.project.model.dto.BizUser;

/**
 * @Author: jiazhuang
 * @Date: 16:46 2018/5/9
 */
public interface LogisticsProviderService {
    /**
     * 新增物流商
     * @param bizUser
     * @return
     */
    int createLogisticsProvider(BizUser bizUser);

    /**
     * 根据账号密码查询用户
     * @param account
     * @param password
     * @return
     */
    BizUser findLogisticsProviderByAccountAndPass(String account, String password);
}