package org.szwj.ca.identityauthsrv.entity.dao;

import java.sql.Timestamp;

public class SignedPDFEntity extends BaseBusinessRecordEntity {

    String BusinessSystemCode;

    String BusinessTypeCode;

    String PdfBasePath;

    String BusinessOrgCode;

    String PdfRelativePath;

    String FileDigest;

    String CertInfoID;

    Timestamp CreatedTime;

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

    public String getFileDigest() {
        return FileDigest;
    }

    public void setFileDigest(String fileDigest) {
        FileDigest = fileDigest;
    }

    public String getCertInfoID() {
        return CertInfoID;
    }

    public void setCertInfoID(String certInfoID) {
        CertInfoID = certInfoID;
    }

    public Timestamp getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        CreatedTime = createdTime;
    }
}
