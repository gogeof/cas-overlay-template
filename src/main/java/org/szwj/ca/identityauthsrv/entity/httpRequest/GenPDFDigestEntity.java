package org.szwj.ca.identityauthsrv.entity.httpRequest;

public class GenPDFDigestEntity extends BaseRequestEntity {

    private String sn;

    private String pdfByte;

    private String signCert;

    private String sealImg;

    private String position;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getPdfByte() {
        return pdfByte;
    }

    public void setPdfByte(String pdfByte) {
        this.pdfByte = pdfByte;
    }

    public String getSignCert() {
        return signCert;
    }

    public void setSignCert(String signCert) {
        this.signCert = signCert;
    }

    public String getSealImg() {
        return sealImg;
    }

    public void setSealImg(String sealImg) {
        this.sealImg = sealImg;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
