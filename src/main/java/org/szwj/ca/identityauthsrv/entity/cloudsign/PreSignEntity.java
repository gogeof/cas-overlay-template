package org.szwj.ca.identityauthsrv.entity.cloudsign;

public class PreSignEntity {

    //业务系统编号
    private String businessSystemCode;

    //预签署信息标题
    private String name;

    //预签署信息内容（xml格式）
    private String data;

    public String getBusinessSystemCode() {
        return businessSystemCode;
    }

    public void setBusinessSystemCode(String businessSystemCode) {
        this.businessSystemCode = businessSystemCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
