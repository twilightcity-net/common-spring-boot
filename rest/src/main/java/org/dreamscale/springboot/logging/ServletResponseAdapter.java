package org.dreamscale.springboot.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.dreamscale.logging.LoggerSupport;
import org.dreamscale.logging.ResponseAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@AllArgsConstructor
public class ServletResponseAdapter implements ResponseAdapter {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private ObjectMapper objectMapper;
    private Object body;

    @Override
    public String getMethod() {
        return request.getMethod();
    }

    @Override
    public String getRequestURI() {
        return request.getRequestURI();
    }

    @Override
    public int getStatusCode() {
        return response.getStatus();
    }

    @Override
    public Object getResponseBody() {
        return body;
    }

    @Override
    public String getResponsePayload(LoggerSupport loggerSupport) throws IOException {
        if (body == null) {
            return null;
        }
        PayloadTruncatingOutputStream stream = new PayloadTruncatingOutputStream(loggerSupport);
        objectMapper.writeValue(stream, body);
        String encoding = response.getCharacterEncoding();
        return stream.getPayload(encoding);
    }

    private static class PayloadTruncatingOutputStream extends OutputStream {
        private LoggerSupport loggerSupport;
        private ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PayloadTruncatingOutputStream(LoggerSupport loggerSupport) {
            this.loggerSupport = loggerSupport;
        }

        String getPayload(String charset) throws IOException {
            byte[] entity = baos.toByteArray();
            return loggerSupport.getPayloadString(entity, charset);
        }

        @Override
        public void write(int i) {
            // write one more byte over maxEntitySize so getPayloadString will correctly truncate
            if (baos.size() <= loggerSupport.getMaxEntitySize()) {
                baos.write(i);
            }
        }
    }

}
