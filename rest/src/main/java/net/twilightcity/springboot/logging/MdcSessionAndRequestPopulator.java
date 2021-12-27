package net.twilightcity.springboot.logging;

import net.twilightcity.logging.LoggingContext;
import net.twilightcity.logging.RequestId;
import net.twilightcity.logging.SessionId;

import javax.servlet.http.HttpServletRequest;

public class MdcSessionAndRequestPopulator implements MdcPopulator {

    @Override
    public void populate(HttpServletRequest request, LoggingContext.Builder contextBuilder) {
        contextBuilder.putIfNotNull(RequestId.MDC_KEY, RequestId.get());
        contextBuilder.putIfNotNull(SessionId.MDC_KEY, SessionId.get());
    }

}
