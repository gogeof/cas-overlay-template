package org.szwj.ca.identityauthsrv.entity.httpRequest;

public class BaseRequestEntity {

    private String businessSystemCode;

    private String businessTypeCode;

    private String authority;

    public String getBusinessSystemCode() {
        return businessSystemCode;
    }

    public void setBusinessSystemCode(String businessSystemCode) {
        this.businessSystemCode = businessSystemCode;
    }

    public String getBusinessTypeCode() {
        return businessTypeCode;
    }

    public void setBusinessTypeCode(String businessTypeCode) {
        this.businessTypeCode = businessTypeCode;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
