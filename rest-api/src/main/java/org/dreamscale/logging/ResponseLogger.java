package org.dreamscale.logging;

import lombok.AllArgsConstructor;
import org.dreamscale.exception.ErrorEntity;
import org.dreamscale.http.HttpStatus;
import org.slf4j.Logger;

import java.io.IOException;

@AllArgsConstructor
public class ResponseLogger {

    private ResponseAdapter response;
    private LoggerSupport loggerSupport;
    private Logger log;
    private String id;

    public void logResponse() {
        if (log.isInfoEnabled()) {
            StringBuilder logResponseBuilder = new StringBuilder();
            appendResponseInfo(logResponseBuilder);
            appendResponsePayload(logResponseBuilder);

            if (logResponseBuilder.length() > 0) {
                log.info(logResponseBuilder.toString());
            }
        }
    }

    private void appendResponseInfo(StringBuilder builder) {
        builder.append(id).append(" Response: [")
                .append(response.getMethod()).append(" ")
                .append(response.getRequestURI())
                .append(" < ").append(response.getStatusCode())
                .append("]");
    }

    private void appendResponsePayload(StringBuilder builder) {
        if (loggerSupport.shouldLogPayload()) {
            LoggingLevel payloadLogLevel = getResponsePayloadLogLevel();
            if (loggerSupport.isLogLevelEnabled(log, payloadLogLevel)) {
                String payload = getResponsePayload(response);
                builder.append(", Payload: [").append(payload).append("]");
            }
        }
    }

    private String getResponsePayload(ResponseAdapter response) {
        try {
            String payload = response.getResponsePayload(loggerSupport);
            return payload == null ? "" : payload.trim();
        } catch (IOException ex) {
            log.warn("Failed to convert request response payload to string", ex);
            return "unknown";
        }
    }

    private LoggingLevel getResponsePayloadLogLevel() {
        LoggingLevel errorEntityLogLevel = getLogLevelFromErrorEntity(response.getResponseBody());
        if (errorEntityLogLevel != LoggingLevel.NONE) {
            return errorEntityLogLevel;
        } else {
            int statusCode = response.getStatusCode();
            if (isErrorStatus(statusCode)) {
                return LoggingLevel.ERROR;
            } else if (isWarnStatus(statusCode) && statusCode != HttpStatus.SC_NOT_FOUND) {
                return LoggingLevel.WARN;
            }
        }
        return loggerSupport.getPayloadLogLevelBasedOnReqMethod(response.getMethod());
    }

    private boolean isErrorStatus(int statusCode) {
        return (statusCode % 500) < 100;
    }

    private boolean isWarnStatus(int statusCode) {
        return (statusCode % 400) < 100;
    }

    private LoggingLevel getLogLevelFromErrorEntity(Object responseBody) {
        if (responseBody instanceof ErrorEntity) {
            LoggingLevel level = ((ErrorEntity) responseBody).getLogLevel();
            if (level != null) {
                return level;
            }
        }
        return LoggingLevel.NONE;
    }

}
