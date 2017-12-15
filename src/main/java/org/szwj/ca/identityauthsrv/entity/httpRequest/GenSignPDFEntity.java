package org.szwj.ca.identityauthsrv.entity.httpRequest;

public class GenSignPDFEntity extends BaseRequestEntity {

    private String signedDigest;

    public String getSignedDigest() {
        return signedDigest;
    }

    public void setSignedDigest(String signedDigest) {
        this.signedDigest = signedDigest;
    }
}
