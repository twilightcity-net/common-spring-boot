package org.dreamscale.logging;

import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class RequestLogger {

    private RequestAdapter request;
    private LoggerSupport loggerSupport;
    private Logger log;
    private String id;
    private Set<String> excludedHeaderKeys;
    private Set<String> includedHeaderKeys;

    public void logRequest() {
        StringBuilder requestLogBuilder = new StringBuilder();
        if (log.isInfoEnabled()) {
            appendRequestInfo(requestLogBuilder);
            appendRequestPayload(requestLogBuilder);
            appendRequestHeaders(requestLogBuilder);

            if (requestLogBuilder.length() > 0) {
                log.info(requestLogBuilder.toString());
            }
        }
    }

    private void appendRequestInfo(StringBuilder builder) {
        builder.append(id).append(" Request: [")
                .append(request.getMethod()).append(" ")
                .append(request.getRequestURI())
                .append("]");
    }

    private void appendRequestPayload(StringBuilder requestLogBuilder) {
        if (shouldAppendRequestPayload()) {
            String requestPayload;
            if (isPayloadReadableContentType()) {
                requestPayload = getRequestPayload().trim();
            } else {
                requestPayload = "Content-Type not readable";
            }
            requestLogBuilder.append(", Payload: [")
                    .append(requestPayload)
                    .append("]");
        }
    }

    private boolean shouldAppendRequestPayload() {
        if (loggerSupport.shouldLogPayload()) {
            LoggingLevel logLevel = loggerSupport.getPayloadLogLevelBasedOnReqMethod(request.getMethod());
            return loggerSupport.isLogLevelEnabled(log, logLevel);
        }
        return false;
    }

    private boolean isPayloadReadableContentType() {
        Collection<String> contentTypeHeaders = request.getHeaders(HttpHeaders.CONTENT_TYPE);
        if (contentTypeHeaders != null) {
            for (String header : contentTypeHeaders) {
                MediaType mediaType = MediaType.parse(header);
                if (loggerSupport.isReadableMediaType(mediaType)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String getRequestPayload() {
        try {
            String payload = request.getRequestPayload(loggerSupport);
            return payload == null ? "" : payload.trim();
        } catch (IOException ex) {
            log.warn("Failed to convert request payload to string", ex);
            return "unknown";
        }
    }

    private void appendRequestHeaders(StringBuilder builder) {
        if (log.isDebugEnabled()) {
            List<Header> headers = new ArrayList<>();

            for (String headerName : request.getHeaderNames()) {
                if (shouldLogHeader(headerName)) {
                    Collection<String> headerValues = request.getHeaders(headerName);
                    headers.add(new Header(headerName, headerValues));
                }
            }

            if (headers.isEmpty() == false) {
                builder.append(", Headers: ").append(headers);
            }
        }
    }

    private boolean shouldLogHeader(String key) {
        if (excludedHeaderKeys.contains(key)) {
            return false;
        } else {
            return includedHeaderKeys.isEmpty() || includedHeaderKeys.contains(key);
        }
    }


    private static class Header {

        private String key;
        private Collection<String> values;

        Header(String key, Collection<String> values) {
            this.key = key;
            this.values = values;
        }

        @Override
        public String toString() {
            String valueString = null;
            if (values.size() == 1) {
                valueString = values.iterator().next();
            } else if (values.size() > 1) {
                valueString = values.toString();
            }
            return key + "=" + valueString;
        }
    }

}
