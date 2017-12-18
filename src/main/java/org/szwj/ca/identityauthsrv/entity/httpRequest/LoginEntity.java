package org.szwj.ca.identityauthsrv.entity.httpRequest;

/**
 * 请求登录动态口令消息类
 */
public class LoginEntity extends BaseRequestEntity {

    private String sn;

    private String signCert;

    private String cryptionCert;

    private String passwd;

    private String loginInfoID;

    private String sourceData;

    private String signedData;

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

    public String getCryptionCert() {
        return cryptionCert;
    }

    public void setCryptionCert(String cryptionCert) {
        this.cryptionCert = cryptionCert;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getLoginInfoID() {
        return loginInfoID;
    }

    public void setLoginInfoID(String loginInfoID) {
        this.loginInfoID = loginInfoID;
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
}
