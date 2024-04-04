package org.example.double_it_sts;

import org.apache.wss4j.common.ext.WSPasswordCallback;

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
                    if ("client".equals(pc.getIdentifier())) {
                        pc.setPassword("changeit");
                    }
                    if ("sts".equals(pc.getIdentifier())) {
                        pc.setPassword("sts");
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

    @Override
    public String toString() {
        return "CallbackHandler{" +
               "usage='" + usage + '\'' +
               '}';
    }
}
