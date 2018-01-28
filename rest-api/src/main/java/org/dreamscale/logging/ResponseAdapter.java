package org.dreamscale.logging;

import java.io.IOException;

public interface ResponseAdapter {

    String getMethod();

    String getRequestURI();

    int getStatusCode();

    /**
     * This is an optimization provided for server-side requests, specifically to resolve any ErrorEntity object for
     * the purpose of determining log level.  This avoids having to convert the ErrorEntity from and then back to json,
     * just to determine the internal error level, if any.
     */
    default Object getResponseBody() {
        return null;
    }

    String getResponsePayload() throws IOException;

}
