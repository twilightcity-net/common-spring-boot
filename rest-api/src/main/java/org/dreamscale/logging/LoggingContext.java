package org.dreamscale.logging;

import org.slf4j.MDC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LoggingContext implements AutoCloseable {

    private List<MDC.MDCCloseable> closeables = new ArrayList<>();

    private LoggingContext(Map<String,String> context) {
        closeables.addAll(context.entrySet().stream()
                .map(entry -> MDC.putCloseable(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList()));
    }

    @Override
    public void close() {
        closeables.forEach(MDC.MDCCloseable::close);
    }

    public static class Builder {

        private Map<String, String> context = new HashMap<>();

        public LoggingContext.Builder put(String key, String value) {
            context.put(key, value);
            return this;
        }

        public LoggingContext.Builder putIfNotNull(String key, String value) {
            if (value != null) {
                context.put(key, value);
            }
            return this;
        }

        public LoggingContext build() {
            return new LoggingContext(context);

        }

    }

}
