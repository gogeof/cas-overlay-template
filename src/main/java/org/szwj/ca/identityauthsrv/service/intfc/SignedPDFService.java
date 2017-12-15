package org.szwj.ca.identityauthsrv.service.intfc;

import org.szwj.ca.identityauthsrv.entity.dao.SignedPDFEntity;

public interface SignedPDFService {

    /**
     * 插入签章信息记录
     *
     * @param signedPDFEntity 签章信息实体类
     */
    void InsertSignedPDFRecord(SignedPDFEntity signedPDFEntity);
}
