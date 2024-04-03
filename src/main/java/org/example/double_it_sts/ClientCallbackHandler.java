package org.example.double_it_sts;

import org.apache.wss4j.common.ext.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;

public class ClientCallbackHandler implements CallbackHandler {

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
            }
        }
    }
}
