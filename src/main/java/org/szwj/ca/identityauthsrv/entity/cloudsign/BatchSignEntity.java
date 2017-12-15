package org.szwj.ca.identityauthsrv.entity.cloudsign;

import java.util.List;

public class BatchSignEntity {

    private String businessSystemCode;

    private String account;

    private List<String> preSignIDs;

    private String signTitle;

    private String remark;

    private String notifyUrl;

    public String getBusinessSystemCode() {
        return businessSystemCode;
    }

    public void setBusinessSystemCode(String businessSystemCode) {
        this.businessSystemCode = businessSystemCode;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public List<String> getPreSignIDs() {
        return preSignIDs;
    }

    public void setPreSignIDs(List<String> preSignIDs) {
        this.preSignIDs = preSignIDs;
    }

    public String getSignTitle() {
        return signTitle;
    }

    public void setSignTitle(String signTitle) {
        this.signTitle = signTitle;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
}
