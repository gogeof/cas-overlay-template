package org.szwj.ca.identityauthsrv.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.szwj.ca.identityauthsrv.dao.SignedPDFDAO;
import org.szwj.ca.identityauthsrv.entity.dao.SignedPDFEntity;
import org.szwj.ca.identityauthsrv.service.intfc.SignedPDFService;

@Service("signedPDFService")
public class SignedPDFServiceImpl implements SignedPDFService {

    private final SignedPDFDAO signedPDFDAO;

    @Autowired
    public SignedPDFServiceImpl(SignedPDFDAO signedPDFDAO) {
        this.signedPDFDAO = signedPDFDAO;
    }

    @Override
    public void InsertSignedPDFRecord(SignedPDFEntity signedPDFEntity) {
        signedPDFDAO.InsertSignedPDFRecord(signedPDFEntity);
    }
}
