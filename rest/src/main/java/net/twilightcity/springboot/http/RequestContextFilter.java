package net.twilightcity.springboot.http;

import net.twilightcity.http.RequestContext;
import net.twilightcity.logging.RequestId;
import net.twilightcity.logging.SessionId;
import net.twilightcity.springboot.FilterPrecedence;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Order(FilterPrecedence.REQUEST_CONTEXT_FILTER)
public class RequestContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            RequestContext.set(createRequestContext(request));
            filterChain.doFilter(request, response);
            setRequestAndSessionIdAsResponseHeaders(response);
        } finally {
            RequestContext.clear();
        }
    }

    private RequestContext createRequestContext(HttpServletRequest request) {
        String requestId = getOrInitializeRequestId(request);
        String sessionId = getSessionId(request);
        return RequestContext.builder()
                .requestId(requestId)
                .sessionId(sessionId)
                .build();
    }

    private String getOrInitializeRequestId(HttpServletRequest req) {
        String requestId = req.getHeader(RequestId.HEADER);
        if (StringUtils.isEmpty(requestId)) {
            requestId = UUID.randomUUID().toString();
        }
        return requestId;
    }

    private String getSessionId(HttpServletRequest req) {
        String sessionId = req.getHeader(SessionId.HEADER);
        return StringUtils.isEmpty(sessionId) ? null : sessionId;
    }

    private void setRequestAndSessionIdAsResponseHeaders(HttpServletResponse response) throws IOException {
        RequestContext context = RequestContext.get();
        String requestId = context.getRequestId();
        if (requestId != null) {
            response.setHeader(RequestId.HEADER, requestId);
        }

        String sessionId = context.getSessionId();
        if (sessionId != null) {
            response.setHeader(SessionId.HEADER, sessionId);
        }
    }

}
