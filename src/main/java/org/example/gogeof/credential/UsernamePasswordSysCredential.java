package org.example.gogeof.credential;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apereo.cas.authentication.RememberMeUsernamePasswordCredential;

import javax.validation.constraints.Size;

public class UsernamePasswordSysCredential extends RememberMeUsernamePasswordCredential {
    @Size(min = 2, message = "require system")
    private String system;

    public String getSystem(){
        return this.system;
    }

    public UsernamePasswordSysCredential setSystem(String system) {
        this.system = system;
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(this.system)
                .toHashCode();
    }
}
