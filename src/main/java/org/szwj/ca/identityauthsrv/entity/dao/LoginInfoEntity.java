package org.szwj.ca.identityauthsrv.entity.dao;

import java.sql.Timestamp;

public class LoginInfoEntity extends BaseBusinessRecordEntity {

    String BusinessSystemCode;

    String BusinessTypeCode;

    String RandomNum;

    Timestamp CreatedTime;

    String SignedData;

    String CertInfoID;

    String EncryptedToken;

    Timestamp ExpiredTime;

    public String getBusinessSystemCode() {
        return BusinessSystemCode;
    }

    public void setBusinessSystemCode(String businessSystemCode) {
        BusinessSystemCode = businessSystemCode;
    }

    public String getBusinessTypeCode() {
        return BusinessTypeCode;
    }

    public void setBusinessTypeCode(String businessTypeCode) {
        BusinessTypeCode = businessTypeCode;
    }

    public String getRandomNum() {
        return RandomNum;
    }

    public void setRandomNum(String randomNum) {
        this.RandomNum = randomNum;
    }

    public Timestamp getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        CreatedTime = createdTime;
    }

    public String getSignedData() {
        return SignedData;
    }

    public void setSignedData(String signedData) {
        this.SignedData = signedData;
    }

    public String getCertInfoID() {
        return CertInfoID;
    }

    public void setCertInfoID(String certInfoID) {
        CertInfoID = certInfoID;
    }

    public String getEncryptedToken() {
        return EncryptedToken;
    }

    public void setEncryptedToken(String encryptedToken) {
        EncryptedToken = encryptedToken;
    }

    public Timestamp getExpiredTime() {
        return ExpiredTime;
    }

    public void setExpiredTime(Timestamp expiredTime) {
        ExpiredTime = expiredTime;
    }
}
