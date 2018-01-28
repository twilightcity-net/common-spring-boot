package org.dreamscale.logging;

import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.Set;

@Builder(toBuilder = true)
@AllArgsConstructor
public class RequestResponseLoggerFactory {

    private Set<String> excludedHeaderKeys;
    private Set<String> includedHeaderKeys;
    private LoggerSupport loggerSupport;
    private String id;

    public RequestResponseLoggerFactory() {
        excludedHeaderKeys = Collections.EMPTY_SET;
        includedHeaderKeys = Collections.EMPTY_SET;
        loggerSupport = new LoggerSupport();
    }

    private Set<String> addItemToSet(Set<String> set, String item) {
        return new ImmutableSet.Builder<String>()
                .addAll(set)
                .add(item)
                .build();
    }

    public RequestResponseLoggerFactory excludeHeader(String key) {
        Set<String> excludedKeys = addItemToSet(excludedHeaderKeys, key);
        return toBuilder()
                .excludedHeaderKeys(excludedKeys)
                .build();
    }

    public RequestResponseLoggerFactory includeHeader(String key) {
        Set<String> includedKeys = addItemToSet(includedHeaderKeys, key);
        return toBuilder()
                .includedHeaderKeys(includedKeys)
                .build();
    }

    public RequestResponseLoggerFactory excludePayload() {
        return toBuilder()
                .loggerSupport(loggerSupport.excludePayload())
                .build();
    }

    public RequestResponseLoggerFactory maxEntitySize(int maxEntitySize) {
        return toBuilder()
                .loggerSupport(loggerSupport.maxEntitySize(maxEntitySize))
                .build();
    }

    public RequestResponseLoggerFactory id(String id) {
        return toBuilder()
                .id(id)
                .build();
    }

    public RequestLogger createRequestLogger(RequestAdapter requestAdapter, Logger log) {
        return new RequestLogger(requestAdapter, loggerSupport, log, id, excludedHeaderKeys, includedHeaderKeys);
    }

    public ResponseLogger createResponseLogger(ResponseAdapter responseAdapter, Logger log) {
        return new ResponseLogger(responseAdapter, loggerSupport, log, id);
    }

}
