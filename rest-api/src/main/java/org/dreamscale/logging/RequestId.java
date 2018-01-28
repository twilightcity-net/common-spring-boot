package org.dreamscale.logging;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.dreamscale.http.RequestContext;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestId {
    public static final String HEADER = "X-Request-Id";
    public static final String MDC_KEY = "requestid";

    public static String get() {
        String requestId = null;
        if (RequestContext.get() != null) {
            requestId = RequestContext.get().getRequestId();
        }
        return isBlank(requestId) ? null : requestId;
    }

    private static boolean isBlank(String string) {
        return string == null || string.trim().isEmpty();
    }

}
