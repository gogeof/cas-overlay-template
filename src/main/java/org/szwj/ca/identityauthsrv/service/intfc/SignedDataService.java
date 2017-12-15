package org.szwj.ca.identityauthsrv.service.intfc;

import org.szwj.ca.identityauthsrv.entity.dao.SignedDataEntity;

public interface SignedDataService {

    /**
     * 记录签名数据
     *
     * @param signedDataEntity 签名数据实体类
     */
    void InsertSignedDataRecord(SignedDataEntity signedDataEntity);
}
