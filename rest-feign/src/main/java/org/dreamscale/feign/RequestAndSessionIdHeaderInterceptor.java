package org.dreamscale.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.dreamscale.logging.RequestId;
import org.dreamscale.logging.SessionId;

@Slf4j
public class RequestAndSessionIdHeaderInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        addRequestId(template);
    }

    private void addRequestId(RequestTemplate template) {
        String requestId = RequestId.get();
        if (requestId != null) {
            template.header(RequestId.HEADER, requestId);
        }

        String sessionId = SessionId.get();
        if (sessionId != null) {
            template.header(SessionId.HEADER, sessionId);
        }
    }

}
