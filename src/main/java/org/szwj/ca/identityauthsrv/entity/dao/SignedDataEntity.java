package org.szwj.ca.identityauthsrv.entity.dao;

import java.sql.Timestamp;

public class SignedDataEntity extends BaseBusinessRecordEntity {

    private String SourceData;

    private String SignedData;

    private Integer Detach;

    private String CertInfoID;

    private String PatientID;

    private Timestamp CreatedTime;

    public String getSourceData() {
        return SourceData;
    }

    public void setSourceData(String sourceData) {
        SourceData = sourceData;
    }

    public String getSignedData() {
        return SignedData;
    }

    public void setSignedData(String signedData) {
        SignedData = signedData;
    }

    public Integer getDetach() {
        return Detach;
    }

    public void setDetach(Integer detach) {
        Detach = detach;
    }

    public String getCertInfoID() {
        return CertInfoID;
    }

    public void setCertInfoID(String certInfoID) {
        CertInfoID = certInfoID;
    }

    public String getPatientID() {
        return PatientID;
    }

    public void setPatientID(String patientID) {
        PatientID = patientID;
    }

    public Timestamp getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        CreatedTime = createdTime;
    }
}
