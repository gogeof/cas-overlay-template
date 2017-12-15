package org.szwj.ca.identityauthsrv.dao;

import org.szwj.ca.identityauthsrv.entity.dao.LoginInfoEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginInfoDAO {

    // 插入一行新数据
    @Insert(
        "INSERT INTO TLK_LOGIN_INFO(ID,BusinessSystemCode,BusinessTypeCode,Authority,RandomNum) "
            + "VALUES(#{loginInfoEntity.ID},#{loginInfoEntity.BusinessSystemCode},#{loginInfoEntity.BusinessTypeCode},"
            + "#{loginInfoEntity.Authority},#{loginInfoEntity.RandomNum})")
    void InsertRandomNumRecord(@Param("loginInfoEntity") LoginInfoEntity loginInfoEntity);

    // 根据ID查询用户登录信息实体类
    @Select("SELECT * FROM TLK_LOGIN_INFO WHERE ID = #{ID}")
    @ResultType(LoginInfoEntity.class)
    LoginInfoEntity QueryLoginInfoByID(String ID);

    @Update(
        "UPDATE TLK_LOGIN_INFO SET SignedData = #{loginInfoEntity.SignedData},CertInfoID = #{loginInfoEntity.CertInfoID},"
            + "EncryptedToken = #{loginInfoEntity.EncryptedToken},ExpiredTime = #{loginInfoEntity.ExpiredTime} "
            + "WHERE ID = #{loginInfoEntity.ID}")
    void UpdateEncryptedToken(@Param("loginInfoEntity") LoginInfoEntity loginInfoEntity);

    // 根据动态口令查询最后登录记录
    @Select("SELECT * FROM TLK_LOGIN_INFO WHERE EncryptedToken = #{encryptedToken} ORDER BY ExpiredTime DESC LIMIT 1")
    @ResultType(LoginInfoEntity.class)
    LoginInfoEntity QueryLastLoginInfoByEncryptedToken(String encryptedToken);

    // 插入动态口令刷新记录
    @Insert(
        "INSERT INTO TLK_LOGIN_INFO(ID,BusinessSystemCode,BusinessTypeCode,Authority,EncryptedToken,ExpiredTime) "
            + "VALUES(#{loginInfoEntity.ID},#{loginInfoEntity.BusinessSystemCode},#{loginInfoEntity.BusinessTypeCode},"
            + "#{loginInfoEntity.Authority},#{loginInfoEntity.EncryptedToken},#{loginInfoEntity.ExpiredTime})")
    void InsertEncryptedTokenRecord(@Param("loginInfoEntity") LoginInfoEntity loginInfoEntity);
}
