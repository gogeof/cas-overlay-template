package org.szwj.ca.identityauthsrv.entity.httpRequest;

public class VerifyTokenEntity extends BaseRequestEntity {
    private String encryptedToken;

    public String getEncryptedToken() {
        return encryptedToken;
    }

    public void setEncryptedToken(String encryptedToken) {
        this.encryptedToken = encryptedToken;
    }
}
