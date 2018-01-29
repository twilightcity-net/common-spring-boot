package org.dreamscale.springboot.logging;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dreamscale.logging.LoggerSupport;
import org.dreamscale.logging.RequestAdapter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Slf4j
@AllArgsConstructor
public class ServletRequestAdapter implements RequestAdapter {

    private static final String DEFAULT_ENCODING = "UTF-8";
    private PayloadRecordingRequestWrapper request;

    public ServletRequestAdapter(HttpServletRequest request) {
        this.request = new PayloadRecordingRequestWrapper(request);
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    @Override
    public String getMethod() {
        return request.getMethod();
    }

    @Override
    public String getRequestURI() {
        return request.getRequestURI();
    }

    @Override
    public String getRequestPayload(LoggerSupport loggerSupport) throws IOException {
        return request.getPayload(loggerSupport);
    }

    @Override
    public Collection<String> getHeaderNames() {
        return Collections.list(request.getHeaderNames());
    }

    @Override
    public Collection<String> getHeaders(String key) {
        return Collections.list(request.getHeaders(key));
    }


    private static class PayloadRecordingRequestWrapper extends HttpServletRequestWrapper {

        private PayloadRecordingServletInputStream payloadRecordingInputStream;

        PayloadRecordingRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        String getPayload(LoggerSupport loggerSupport) throws IOException {
            String encoding = getCharacterEncoding();
            return ((PayloadRecordingServletInputStream) getInputStream()).getPayload(loggerSupport, encoding);
        }

        @Override
        public String getCharacterEncoding() {
            String encoding = super.getCharacterEncoding();
            return encoding != null ? encoding : DEFAULT_ENCODING;
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            if (payloadRecordingInputStream == null) {
                payloadRecordingInputStream = new PayloadRecordingServletInputStream(super.getInputStream());
            }
            return payloadRecordingInputStream;
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncoding()));
        }

    }

    @Slf4j
    private static class PayloadRecordingServletInputStream extends ServletInputStream {

        private ServletInputStream delegate;
        private BufferedInputStream bufferedInputStream;
        private boolean done = false;

        PayloadRecordingServletInputStream(ServletInputStream delegate) {
            this.delegate = delegate;
            this.bufferedInputStream = new BufferedInputStream(delegate);
        }

        String getPayload(LoggerSupport loggerSupport, String charset) throws IOException {
            try {
                int maxEntitySize = loggerSupport.getMaxEntitySize();
                bufferedInputStream.mark(maxEntitySize + 1);
                // maxEntitySize + 1 so getPayloadString will correctly truncate
                byte[] entity = new byte[maxEntitySize + 1];
                int entitySize = bufferedInputStream.read(entity);
                entitySize = entitySize < 0 ? 0 : entitySize;
                entity = Arrays.copyOf(entity, entitySize);
                return loggerSupport.getPayloadString(entity, charset);
            } finally {
                try {
                    bufferedInputStream.reset();
                } catch (IOException ex) {
                    log.warn("Failed to reset payload input stream", ex);
                }
            }
        }

        @Override
        public boolean isFinished() {
            return done;
        }

        @Override
        public boolean isReady() {
            return done == false;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            delegate.setReadListener(readListener);
        }

        @Override
        public int read() throws IOException {
            int b = bufferedInputStream.read();
            if (b == -1) {
                done = true;
            }
            return b;
        }

    }

}
