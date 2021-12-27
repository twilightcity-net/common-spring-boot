package net.twilightcity.feign;

import feign.Request;
import feign.Response;
import feign.Util;
import lombok.AllArgsConstructor;
import net.twilightcity.http.HttpStatus;
import net.twilightcity.logging.LoggerSupport;
import net.twilightcity.logging.RequestAdapter;
import net.twilightcity.logging.RequestResponseLoggerFactory;
import net.twilightcity.logging.ResponseAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class FeignLogger extends feign.Logger {

    private String loggerPrefix;
    private RequestResponseLoggerFactory loggerFactory;

    public FeignLogger(RequestResponseLoggerFactory loggerFactory, Class clazz) {
        this.loggerFactory = loggerFactory.id("Feign client");
        this.loggerPrefix = clazz.getName();
    }

    private Logger getLogger(String configKey) {
        int index = configKey.indexOf("#");
        if (index > 0) {
            configKey = configKey.substring(index + 1);
        }
        return LoggerFactory.getLogger(loggerPrefix + "." + configKey);
    }

    protected void log(String configKey, String format, Object... args) {
        getLogger(configKey).info(String.format(format, args));
    }

    protected void logRequest(String configKey, Level logLevel, Request request) {
        FeignRequestAdapter requestAdapter = new FeignRequestAdapter(request);
        Logger logger = getLogger(configKey);
        loggerFactory.createRequestLogger(requestAdapter, logger).logRequest();
    }

    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response,
                                              long elapsedTime) {
        FeignResponseAdapter responseAdapter = new FeignResponseAdapter(response);
        Logger logger = getLogger(configKey);
        loggerFactory.createResponseLogger(responseAdapter, logger).logResponse();
        if (responseAdapter.bodyData != null) {
            return response.toBuilder().body(responseAdapter.bodyData).build();
        }
        return response;
    }

    @AllArgsConstructor
    private class FeignRequestAdapter implements RequestAdapter {

        private Request request;

        @Override
        public String getMethod() {
            return request.method();
        }

        @Override
        public String getRequestURI() {
            return request.url();
        }

        @Override
        public String getRequestPayload(LoggerSupport loggerSupport) throws IOException {
            if (request.body() != null) {
                return loggerSupport.getPayloadString(request.body(), request.charset());
            }
            return null;
        }

        @Override
        public Collection<String> getHeaderNames() {
            return request.headers().keySet();
        }

        @Override
        public Collection<String> getHeaders(String key) {
            return request.headers().get(key);
        }

    }

    private class FeignResponseAdapter implements ResponseAdapter {

        private Response response;
        private byte[] bodyData;

        public FeignResponseAdapter(Response response) {
            this.response = response;
        }

        @Override
        public String getMethod() {
            return response.request().method();
        }

        @Override
        public String getRequestURI() {
            return response.request().url();
        }

        @Override
        public int getStatusCode() {
            return response.status();
        }

        @Override
        public String getResponsePayload(LoggerSupport loggerSupport) throws IOException {
            if (shouldLogResponsePaylod()) {
                bodyData = Util.toByteArray(response.body().asInputStream());
                return loggerSupport.getPayloadString(bodyData, StandardCharsets.UTF_8);
            }
            return null;
        }

        private boolean shouldLogResponsePaylod() {
            int status = getStatusCode();
            // HTTP 204 No Content "...response MUST NOT include a message-body"
            // HTTP 205 Reset Content "...response MUST NOT include an entity"
            return response.body() != null && !(status == HttpStatus.SC_NO_CONTENT || status == HttpStatus.SC_RESET_CONTENT);
        }
    }

}
