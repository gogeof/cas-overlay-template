package org.szwj.ca.identityauthsrv.dao;

import org.szwj.ca.identityauthsrv.entity.dao.SignedPDFEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SignedPDFDAO {

    // 记录PDF签章信息
    @Insert(
        "INSERT INTO TLK_SIGNED_FILE(ID,BusinessSystemCode,BusinessTypeCode,Authority,PdfBasePath,BusinessOrgCode,"
            + "PdfRelativePath,FileDigest,CertInfoID,CreatedTime) "
            + "VALUES(#{signedPDFEntity.ID},#{signedPDFEntity.BusinessSystemCode},#{signedPDFEntity.BusinessTypeCode},"
            + "#{signedPDFEntity.Authority},#{signedPDFEntity.PdfBasePath},#{signedPDFEntity.BusinessOrgCode},"
            + "#{signedPDFEntity.PdfRelativePath},#{signedPDFEntity.FileDigest},#{signedPDFEntity.CertInfoID},"
            + "#{signedPDFEntity.CreatedTime})")
    void InsertSignedPDFRecord(@Param("signedPDFEntity") SignedPDFEntity signedPDFEntity);
}
