package org.dreamscale.logging;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.dreamscale.http.RequestContext;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionId {
    public static final String HEADER = "X-Session-Id";
    public static final String MDC_KEY = "sessionid";

    public static String get() {
        String sessionId = null;
        if (RequestContext.get() != null) {
            sessionId = RequestContext.get().getSessionId();
        }
        return isBlank(sessionId) ? null : sessionId;
    }

    private static boolean isBlank(String string) {
        return string == null || string.trim().isEmpty();
    }
}
