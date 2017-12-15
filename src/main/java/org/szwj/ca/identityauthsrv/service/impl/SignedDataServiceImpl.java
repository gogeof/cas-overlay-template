package org.szwj.ca.identityauthsrv.service.impl;

import org.szwj.ca.identityauthsrv.dao.SignedDataDAO;
import org.szwj.ca.identityauthsrv.entity.dao.SignedDataEntity;
import org.szwj.ca.identityauthsrv.service.intfc.SignedDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("signedDataService")
public class SignedDataServiceImpl implements SignedDataService {

    private final SignedDataDAO signedDataDAO;

    @Autowired
    public SignedDataServiceImpl(SignedDataDAO signedDataDAO) {
        this.signedDataDAO = signedDataDAO;
    }

    @Override
    public void InsertSignedDataRecord(SignedDataEntity signedDataEntity) {
        signedDataDAO.InsertSignedDataRecord(signedDataEntity);
    }
}
