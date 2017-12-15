package org.szwj.ca.identityauthsrv.entity.cloudsign;

import java.util.List;

/**
 * 统一身份系统 -> 真宜签后台
 * 批量签署任务data请求参数
 * 请求参数用 base64 加密后传入通用请求的 data 属性中
 */
public class BatchSignBackendReqEntity {

    String outSignId;

    String phoneNo;

    List<String> fileUuids;

    String signTitle;

    String remark;

    String notifyUrl;

    public String getOutSignId() {
        return outSignId;
    }

    public void setOutSignId(String outSignId) {
        this.outSignId = outSignId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public List<String> getFileUuids() {
        return fileUuids;
    }

    public void setFileUuids(List<String> fileUuids) {
        this.fileUuids = fileUuids;
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
