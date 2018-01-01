package org.example.gogeof.credential.handler;

import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.HandlerResult;
import org.apereo.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.example.gogeof.credential.CertKeyCredential;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.szwj.ca.identityauthsrv.controller.LoginController;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class CertKeyAuthenticationHandler extends AbstractPreAndPostProcessingAuthenticationHandler {
    public CertKeyAuthenticationHandler(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order) {
        super(name, servicesManager, principalFactory, order);
    }

    @Override
    protected HandlerResult doAuthentication(Credential credential) throws GeneralSecurityException {
        if (!supports(credential)){
            // not CertKeyCredential, return
            throw new AccountNotFoundException("not authenticated by cert");
        }

        // get auth info
        CertKeyCredential certKeyCredential = (CertKeyCredential) credential;

        // either cert or cert's info is empty?
        if (certKeyCredential == null ||
                certKeyCredential.getSn() == null || certKeyCredential.getSn().equals("") ||
                certKeyCredential.getSignCert() == null || certKeyCredential.getSignCert().equals("") ||
                certKeyCredential.getSourceData() == null || certKeyCredential.getSourceData().equals("") ||
                certKeyCredential.getSignedData() == null || certKeyCredential.getSignedData().equals("")) {
            throw new AccountNotFoundException("param check failed");
        }

        // cert is valid?
        try{
            LoginController lc = new LoginController();

            // TODO 获取到的token，为了后续的使用，可考虑另外存储
            HttpEntity he = lc.getToken(certKeyCredential);
            if (false == (he instanceof ResponseEntity)){
                // auth failed
                throw new AccountNotFoundException("param type not right");
            }

            ResponseEntity re = (ResponseEntity) he;
            if (re.getStatusCode().equals("200")){
                // auth success
                return createHandlerResult(credential, this.principalFactory.createPrincipal(((CertKeyCredential) credential).getId(), Collections.<String, Object>emptyMap()), null);
            }
        }catch (Exception e){
            throw new AccountNotFoundException("service internal failed.");
        }

        throw new AccountNotFoundException("not authenticated");
    }

    public boolean supports(Credential credential) {
        return credential instanceof CertKeyCredential;
    }
}
