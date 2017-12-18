package org.szwj.ca.identityauthsrv.entity.cloudsign;

import java.sql.Timestamp;

public class CloudSignBatchTaskEntity {

    private String ID;

    private String BusinessSystemCode;

    private String Account;

    private String PreSignIDs;

    private String SignTitle;

    private String Remark;

    private String NotifyUrl;

    private String BatchSignID;

    private String FinishTimeStr;

    private Integer SignStatus;

    private Integer SignResult;

    private String OutFileIDs;

    // 创建时间
    private Timestamp CreatedTime;

    // 最后修改时间
    private Timestamp LastTime;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getBusinessSystemCode() {
        return BusinessSystemCode;
    }

    public void setBusinessSystemCode(String businessSystemCode) {
        BusinessSystemCode = businessSystemCode;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getPreSignIDs() {
        return PreSignIDs;
    }

    public void setPreSignIDs(String preSignIDs) {
        PreSignIDs = preSignIDs;
    }

    public String getSignTitle() {
        return SignTitle;
    }

    public void setSignTitle(String signTitle) {
        SignTitle = signTitle;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getNotifyUrl() {
        return NotifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        NotifyUrl = notifyUrl;
    }

    public String getBatchSignID() {
        return BatchSignID;
    }

    public void setBatchSignID(String batchSignID) {
        BatchSignID = batchSignID;
    }

    public String getFinishTimeStr() {
        return FinishTimeStr;
    }

    public void setFinishTimeStr(String finishTimeStr) {
        FinishTimeStr = finishTimeStr;
    }

    public Integer getSignStatus() {
        return SignStatus;
    }

    public void setSignStatus(Integer signStatus) {
        SignStatus = signStatus;
    }

    public Integer getSignResult() {
        return SignResult;
    }

    public void setSignResult(Integer signResult) {
        SignResult = signResult;
    }

    public String getOutFileIDs() {
        return OutFileIDs;
    }

    public void setOutFileIDs(String outFileIDs) {
        OutFileIDs = outFileIDs;
    }

    public Timestamp getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        CreatedTime = createdTime;
    }

    public Timestamp getLastTime() {
        return LastTime;
    }

    public void setLastTime(Timestamp lastTime) {
        LastTime = lastTime;
    }
}
