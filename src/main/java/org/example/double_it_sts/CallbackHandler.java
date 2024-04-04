package org.example.double_it_sts;

import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.apache.wss4j.common.saml.SAMLCallback;
import org.apache.wss4j.common.saml.bean.SubjectBean;
import org.opensaml.saml.common.SAMLVersion;

import javax.security.auth.callback.Callback;

public class CallbackHandler implements javax.security.auth.callback.CallbackHandler {

    private final String usage;

    public CallbackHandler(String usage) {
        this.usage = usage;
    }

    public void handle(Callback[] callbacks) {
        for (Callback callback : callbacks) {
            if (callback instanceof WSPasswordCallback pc) {
                if (pc.getUsage() == WSPasswordCallback.DECRYPT ||
                    pc.getUsage() == WSPasswordCallback.SIGNATURE) {
                    // typically X.509 auth only
                    if ("client".equals(pc.getIdentifier())) {
                        pc.setPassword("changeit");
                    }
                } else if (pc.getUsage() == WSPasswordCallback.USERNAME_TOKEN) {
                    // UsernameToken auth only
                    if ("alice".equals(pc.getIdentifier())) {
                        pc.setPassword("clarinet");
                    }
                }
            } else if (callback instanceof SAMLCallback samlCallback) {
                samlCallback.setSamlVersion(SAMLVersion.VERSION_20);
                final var subject = new SubjectBean();
                subject.setSubjectConfirmationMethod("urn:oasis:names:tc:SAML:2.0:cm:holder-of-key");
                samlCallback.setSubject(subject);
                samlCallback.setIssuer("uff");
            }
        }
    }

    @Override
    public String toString() {
        return "CallbackHandler{" +
               "usage='" + usage + '\'' +
               '}';
    }
}
