package org.szwj.ca.identityauthsrv.entity.httpResponse;

import sglca.helper.models.*;

public class EventValueEntity extends EventValue {

    private String loginInfoID;

    private String digest;

    private String base64SealPdf;

    private String boundValue;

    private String preSignID;

    private String batchSignID;

    public String getLoginInfoID() {
        return loginInfoID;
    }

    public void setLoginInfoID(String loginInfoID) {
        this.loginInfoID = loginInfoID;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getBase64SealPdf() {
        return base64SealPdf;
    }

    public void setBase64SealPdf(String base64SealPdf) {
        this.base64SealPdf = base64SealPdf;
    }

    public String getBoundValue() {
        return boundValue;
    }

    public void setBoundValue(String boundValue) {
        this.boundValue = boundValue;
    }

    public String getPreSignID() {
        return preSignID;
    }

    public void setPreSignID(String preSignID) {
        this.preSignID = preSignID;
    }

    public String getBatchSignID() {
        return batchSignID;
    }

    public void setBatchSignID(String batchSignID) {
        this.batchSignID = batchSignID;
    }
}
