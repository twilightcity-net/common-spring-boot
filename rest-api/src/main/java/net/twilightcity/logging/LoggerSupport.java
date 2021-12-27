package net.twilightcity.logging;

import com.google.common.net.MediaType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
public class LoggerSupport {

    private static final int DEFAULT_MAX_ENTITY_SIZE = 1024;

    private int maxEntitySize = DEFAULT_MAX_ENTITY_SIZE;
    private boolean includePayload = true;

    private static final ReadableMediaTypeSupport readableMediaTypeSupport = new ReadableMediaTypeSupport() {{
        addReableMediaType("text/*");
        addReableMediaType("application/json");
        addReableMediaType("application/xml");
        addReableMediaType("application/svg+xml");
        addReableMediaType("application/atom+xml");
        addReableMediaType("application/xhtml+xml");
        addReableMediaType("application/x-www-form-urlencoded");
    }};

    public LoggerSupport excludePayload() {
        return new LoggerSupport(maxEntitySize, false);
    }

    public LoggerSupport maxEntitySize(int maxEntitySize) {
        return new LoggerSupport(maxEntitySize, includePayload);
    }

    public boolean shouldLogPayload() {
        return includePayload;
    }

    public int getMaxEntitySize() {
        return maxEntitySize;
    }

    public String getPayloadString(byte[] entityBytes, Charset charset) throws IOException {
        String charsetName = charset == null ? null : charset.name();
        return getPayloadString(entityBytes, charsetName);
    }

    public String getPayloadString(byte[] entityBytes, String charsetName) throws IOException {
        // null charsetName indicates unknown or not applicable (e.g. binary data)
        if (charsetName == null) {
            return null;
        }

        int entityLength = Math.min(entityBytes.length, getMaxEntitySize());
        StringBuilder b = new StringBuilder(entityLength + 15);

        b.append(new String(entityBytes, 0, entityLength, charsetName));
        if (entityBytes.length > getMaxEntitySize()) {
            b.append("...more...");
        }
        b.append('\n');
        return b.toString();
    }

    public boolean isLogLevelEnabled(Logger log, LoggingLevel logLevel) {
        switch (logLevel) {
            case DEBUG:
                return log.isDebugEnabled();
            case WARN:
                return log.isWarnEnabled();
            case ERROR:
                return log.isErrorEnabled();
            case TRACE:
                return log.isTraceEnabled();
            default:
                return false;
        }
    }

    public boolean isReadableMediaType(MediaType mediaType) {
        return readableMediaTypeSupport.isReadableMediaType(mediaType);
    }

    public LoggingLevel getPayloadLogLevelBasedOnReqMethod(String method) {
        return "GET".equals(method) || "HEAD".equals(method) ? LoggingLevel.TRACE : LoggingLevel.DEBUG;
    }

    private static class ReadableMediaTypeSupport {

        private Set<MediaType> readableMediaTypes = new HashSet<>();

        void addReableMediaType(String mediaTypeValue) {
            MediaType mediaType = MediaType.parse(mediaTypeValue).withoutParameters();
            readableMediaTypes.add(mediaType);
        }

        boolean isReadableMediaType(MediaType mediaType) {
            MediaType noParamMediaType = mediaType.withoutParameters();
            for (MediaType readableMediaType : readableMediaTypes) {
                if (readableMediaType.is(noParamMediaType)) {
                    return true;
                }
            }
            return false;
        }

    }

}
