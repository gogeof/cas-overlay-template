package org.szwj.ca.identityauthsrv.entity.httpRequest;

public class VerifyDataEntity extends BaseRequestEntity {

    private String sn;

    private String signCert;

    private String sourceData;

    private String signedData;

    private Integer detach;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getSignCert() {
        return signCert;
    }

    public void setSignCert(String signCert) {
        this.signCert = signCert;
    }

    public String getSourceData() {
        return sourceData;
    }

    public void setSourceData(String sourceData) {
        this.sourceData = sourceData;
    }

    public String getSignedData() {
        return signedData;
    }

    public void setSignedData(String signedData) {
        this.signedData = signedData;
    }

    public Integer getDetach() {
        return detach;
    }

    public void setDetach(Integer detach) {
        this.detach = detach;
    }
}
