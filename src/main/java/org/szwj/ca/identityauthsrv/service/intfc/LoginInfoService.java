package org.szwj.ca.identityauthsrv.service.intfc;

import org.szwj.ca.identityauthsrv.entity.dao.LoginInfoEntity;

/**
 * LoginInfoService 接口类
 */
public interface LoginInfoService {

    /**
     * 添加随机数请求记录
     *
     * @param loginInfoEntity 登录信息实体类
     */
    void InsertRandomNumRecord(LoginInfoEntity loginInfoEntity);

    /**
     * 根据ID查询用户登录信息实体类
     *
     * @param ID 表ID
     * @return 用户登录信息实体类
     */
    LoginInfoEntity QueryLoginInfoByID(String ID);

    /**
     * 更新动态令牌
     *
     * @param loginInfoEntity 登录信息实体类
     */
    void UpdateEncryptedToken(LoginInfoEntity loginInfoEntity);

    /**
     * 根据动态口令查询最后登录记录
     *
     * @param encryptedToken 动态口令
     * @return 登录信息实体类
     */
    LoginInfoEntity QueryLastLoginInfoByEncryptedToken(String encryptedToken);

    /**
     * 插入动态口令刷新记录
     *
     * @param loginInfoEntity 登录信息实体类
     */
    void InsertEncryptedTokenRecord(LoginInfoEntity loginInfoEntity);
}
