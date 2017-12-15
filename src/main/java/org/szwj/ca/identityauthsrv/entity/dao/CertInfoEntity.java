package org.szwj.ca.identityauthsrv.entity.dao;

import java.sql.Timestamp;

/**
 * 证书信息实体类
 */
public class CertInfoEntity {

    private String ID;

    private String Type;

    private String SerialNumber;

    private Timestamp ValidFromDate;

    private Timestamp ValidToDate;

    private String HardwareCode;

    private String SignCert;

    private String CryptionCert;

    private String Authority;

    private Timestamp CreatedTime;

    private String SignFlow;

    private String UserID;

    private String UserName;

    private String UserDepartment;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }

    public Timestamp getValidFromDate() {
        return ValidFromDate;
    }

    public void setValidFromDate(Timestamp validFromDate) {
        ValidFromDate = validFromDate;
    }

    public Timestamp getValidToDate() {
        return ValidToDate;
    }

    public void setValidToDate(Timestamp validToDate) {
        ValidToDate = validToDate;
    }

    public String getHardwareCode() {
        return HardwareCode;
    }

    public void setHardwareCode(String hardwareCode) {
        HardwareCode = hardwareCode;
    }

    public String getSignCert() {
        return SignCert;
    }

    public void setSignCert(String signCert) {
        SignCert = signCert;
    }

    public String getCryptionCert() {
        return CryptionCert;
    }

    public void setCryptionCert(String cryptionCert) {
        CryptionCert = cryptionCert;
    }

    public String getAuthority() {
        return Authority;
    }

    public void setAuthority(String authority) {
        Authority = authority;
    }

    public String getSignFlow() {
        return SignFlow;
    }

    public void setSignFlow(String signFlow) {
        SignFlow = signFlow;
    }

    public Timestamp getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        CreatedTime = createdTime;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserDepartment() {
        return UserDepartment;
    }

    public void setUserDepartment(String userDepartment) {
        UserDepartment = userDepartment;
    }
}
