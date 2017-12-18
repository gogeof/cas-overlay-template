package org.szwj.ca.identityauthsrv.dao;

import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.szwj.ca.identityauthsrv.entity.dao.CertInfoEntity;

@Repository
public interface CertInfoDAO {

    // 根据SN码获取证书颁发机构Authority
    @Select("SELECT AUTHORITY FROM T_CERT_INFO WHERE SERIALNUMBER = #{sn}")
    @ResultType(String.class)
    String QueryAuthorityBySn(String sn);

    // 根据SN码获取证书信息实体类
    @Select("SELECT * FROM T_CERT_INFO WHERE SERIALNUMBER = #{sn}")
    @ResultType(CertInfoEntity.class)
    CertInfoEntity QueryCertInfoBySn(String sn);
}
