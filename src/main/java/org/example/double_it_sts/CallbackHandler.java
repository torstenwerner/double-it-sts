package org.example.double_it_sts;

import org.apache.wss4j.common.ext.WSPasswordCallback;

import javax.security.auth.callback.Callback;

/**
 * Returns passwords for all keystores and the sample user 'alice'.
 * Separate classes should be implemented for user, client, sts, and service.
 * It is just one class to keep the project simple.
 */
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
                    if ("user".equals(pc.getIdentifier())) {
                        pc.setPassword("changeit");
                    }
                    if ("client".equals(pc.getIdentifier())) {
                        pc.setPassword("changeit");
                    }
                    if ("server".equals(pc.getIdentifier())) {
                        pc.setPassword("changeit");
                    }
                    if ("sts".equals(pc.getIdentifier())) {
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

    @Override
    public String toString() {
        return "CallbackHandler{" +
               "usage='" + usage + '\'' +
               '}';
    }
}
