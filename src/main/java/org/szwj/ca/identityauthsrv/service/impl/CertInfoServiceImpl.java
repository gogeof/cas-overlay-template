package org.szwj.ca.identityauthsrv.service.impl;

import org.szwj.ca.identityauthsrv.dao.CertInfoDAO;
import org.szwj.ca.identityauthsrv.entity.dao.CertInfoEntity;
import org.szwj.ca.identityauthsrv.service.intfc.CertInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("certInfoService")
public class CertInfoServiceImpl implements CertInfoService {

    private final CertInfoDAO certInfoDAO;

    // 依赖注入
    @Autowired
    public CertInfoServiceImpl(CertInfoDAO certInfoDAO) {
        this.certInfoDAO = certInfoDAO;
    }

    @Override
    public String QueryAuthorityBySn(String sn) {
        return certInfoDAO.QueryAuthorityBySn(sn);
    }

    @Override
    public CertInfoEntity QueryCertInfoBySn(String sn) {
        return certInfoDAO.QueryCertInfoBySn(sn);
    }
}
