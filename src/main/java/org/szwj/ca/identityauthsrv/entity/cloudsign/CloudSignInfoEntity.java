package org.szwj.ca.identityauthsrv.entity.cloudsign;

import java.sql.Timestamp;

public class CloudSignInfoEntity {

    String ID;

    String BusinessSystemCode;

    String Data;

    String PdfBasePath;

    String BusinessOrgCode;

    String PdfRelativePath;

    String PreSignID;

    String SignStatus;

    String BatchSignID;

    String FiledId;

    Timestamp CreatedTime;

    Timestamp LastTime;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getBusinessSystemCode() {
        return BusinessSystemCode;
    }

    public void setBusinessSystemCode(String businessSystemCode) {
        BusinessSystemCode = businessSystemCode;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getPdfBasePath() {
        return PdfBasePath;
    }

    public void setPdfBasePath(String pdfBasePath) {
        PdfBasePath = pdfBasePath;
    }

    public String getBusinessOrgCode() {
        return BusinessOrgCode;
    }

    public void setBusinessOrgCode(String businessOrgCode) {
        BusinessOrgCode = businessOrgCode;
    }

    public String getPdfRelativePath() {
        return PdfRelativePath;
    }

    public void setPdfRelativePath(String pdfRelativePath) {
        PdfRelativePath = pdfRelativePath;
    }

    public String getPreSignID() {
        return PreSignID;
    }

    public void setPreSignID(String preSignID) {
        PreSignID = preSignID;
    }

    public String getSignStatus() {
        return SignStatus;
    }

    public void setSignStatus(String signStatus) {
        SignStatus = signStatus;
    }

    public String getBatchSignID() {
        return BatchSignID;
    }

    public void setBatchSignID(String batchSignID) {
        BatchSignID = batchSignID;
    }

    public String getFiledId() {
        return FiledId;
    }

    public void setFiledId(String filedId) {
        FiledId = filedId;
    }

    public Timestamp getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        CreatedTime = createdTime;
    }

    public Timestamp getLastTime() {
        return LastTime;
    }

    public void setLastTime(Timestamp lastTime) {
        LastTime = lastTime;
    }
}
