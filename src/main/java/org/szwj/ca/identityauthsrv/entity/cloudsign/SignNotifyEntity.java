package org.szwj.ca.identityauthsrv.entity.cloudsign;

public class SignNotifyEntity {

    String uuid;

    String outSignId;

    String signTitle;

    String channelStr;

    String finishTimeStr;

    Integer signType;

    String signTypeStr;

    Integer signStatus;

    String statusStr;

    Integer signResult;

    String signResultStr;

    String signInfo;

    String files;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOutSignId() {
        return outSignId;
    }

    public void setOutSignId(String outSignId) {
        this.outSignId = outSignId;
    }

    public String getSignTitle() {
        return signTitle;
    }

    public void setSignTitle(String signTitle) {
        this.signTitle = signTitle;
    }

    public String getChannelStr() {
        return channelStr;
    }

    public void setChannelStr(String channelStr) {
        this.channelStr = channelStr;
    }

    public String getFinishTimeStr() {
        return finishTimeStr;
    }

    public void setFinishTimeStr(String finishTimeStr) {
        this.finishTimeStr = finishTimeStr;
    }

    public Integer getSignType() {
        return signType;
    }

    public void setSignType(Integer signType) {
        this.signType = signType;
    }

    public String getSignTypeStr() {
        return signTypeStr;
    }

    public void setSignTypeStr(String signTypeStr) {
        this.signTypeStr = signTypeStr;
    }

    public Integer getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(Integer signStatus) {
        this.signStatus = signStatus;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public Integer getSignResult() {
        return signResult;
    }

    public void setSignResult(Integer signResult) {
        this.signResult = signResult;
    }

    public String getSignResultStr() {
        return signResultStr;
    }

    public void setSignResultStr(String signResultStr) {
        this.signResultStr = signResultStr;
    }

    public String getSignInfo() {
        return signInfo;
    }

    public void setSignInfo(String signInfo) {
        this.signInfo = signInfo;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }
}