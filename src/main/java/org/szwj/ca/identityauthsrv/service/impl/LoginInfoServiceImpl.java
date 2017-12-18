package org.szwj.ca.identityauthsrv.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.szwj.ca.identityauthsrv.dao.LoginInfoDAO;
import org.szwj.ca.identityauthsrv.entity.dao.LoginInfoEntity;
import org.szwj.ca.identityauthsrv.service.intfc.LoginInfoService;

@Service("loginInfoService")
public class LoginInfoServiceImpl implements LoginInfoService {

    private final LoginInfoDAO loginInfoDAO;

    @Autowired
    public LoginInfoServiceImpl(LoginInfoDAO loginInfoDAO) {
        this.loginInfoDAO = loginInfoDAO;
    }

    @Override
    public void InsertRandomNumRecord(LoginInfoEntity loginInfoEntity) {
        loginInfoDAO.InsertRandomNumRecord(loginInfoEntity);
    }

    @Override
    public LoginInfoEntity QueryLoginInfoByID(String ID) {
        return loginInfoDAO.QueryLoginInfoByID(ID);
    }

    @Override
    public void UpdateEncryptedToken(LoginInfoEntity loginInfoEntity) {
        loginInfoDAO.UpdateEncryptedToken(loginInfoEntity);
    }

    @Override
    public LoginInfoEntity QueryLastLoginInfoByEncryptedToken(String encryptedToken) {
        return loginInfoDAO.QueryLastLoginInfoByEncryptedToken(encryptedToken);
    }

    @Override
    public void InsertEncryptedTokenRecord(LoginInfoEntity loginInfoEntity) {
        loginInfoDAO.InsertEncryptedTokenRecord(loginInfoEntity);
    }
}
