package org.szwj.ca.identityauthsrv.dao;

import org.szwj.ca.identityauthsrv.entity.dao.SignedDataEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SignedDataDAO {

    // 记录签名数据
    @Insert(
        "INSERT INTO TLK_SIGNED_DATA(ID,Authority,SourceData,SignedData,Detach,CertInfoID,PatientID,CreatedTime) "
            + "VALUES(#{signedDataEntity.ID},#{signedDataEntity.Authority},#{signedDataEntity.SourceData},"
            + "#{signedDataEntity.SignedData},#{signedDataEntity.Detach},#{signedDataEntity.CertInfoID},"
            + "#{signedDataEntity.PatientID},#{signedDataEntity.CreatedTime})")
    void InsertSignedDataRecord(@Param("signedDataEntity") SignedDataEntity signedDataEntity);
}
