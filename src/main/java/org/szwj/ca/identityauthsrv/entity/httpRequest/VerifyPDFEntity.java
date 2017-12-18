package org.szwj.ca.identityauthsrv.entity.httpRequest;

public class VerifyPDFEntity extends BaseRequestEntity {

    private String pdfTypeCode;

    private String signCert;

    private String signedPdfStream;

    public String getPdfTypeCode() {
        return pdfTypeCode;
    }

    public void setPdfTypeCode(String pdfTypeCode) {
        this.pdfTypeCode = pdfTypeCode;
    }

    public String getSignCert() {
        return signCert;
    }

    public void setSignCert(String signCert) {
        this.signCert = signCert;
    }

    public String getSignedPdfStream() {
        return signedPdfStream;
    }

    public void setSignedPdfStream(String signedPdfStream) {
        this.signedPdfStream = signedPdfStream;
    }
}
