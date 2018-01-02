package org.example.gogeof.credential;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.szwj.ca.identityauthsrv.entity.httpRequest.LoginEntity;

public class CertKeyCredential extends LoginEntity implements ICertKeyCredential {
    private static final long serialVersionUID = -6710007659431302398L;
    @Override
    public void setUsername(String userName) {
        super.setUsername(userName);
        this.setSn(userName);
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
        this.setPasswd(password);
    }

    @Override
    public String getId() {
        // 返回sn的值作为用户的唯一识别
        return this.getSn();
    }

    @Override
    public boolean isRememberMe() {
        return false;
    }

    @Override
    public void setRememberMe(boolean b) {
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(this.getId())
                .toHashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!super.equals(obj)) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            CertKeyCredential other = (CertKeyCredential)obj;
            return this.getId() == other.getId();
        }
    }
}
