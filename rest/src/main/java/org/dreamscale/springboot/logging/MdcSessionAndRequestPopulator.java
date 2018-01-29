package org.dreamscale.springboot.logging;

import org.dreamscale.logging.LoggingContext;
import org.dreamscale.logging.RequestId;
import org.dreamscale.logging.SessionId;

import javax.servlet.http.HttpServletRequest;

public class MdcSessionAndRequestPopulator implements MdcPopulator {

    @Override
    public void populate(HttpServletRequest request, LoggingContext.Builder contextBuilder) {
        contextBuilder.putIfNotNull(RequestId.MDC_KEY, RequestId.get());
        contextBuilder.putIfNotNull(SessionId.MDC_KEY, SessionId.get());
    }

}
