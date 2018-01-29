package org.dreamscale.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.Util;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dreamscale.exception.ErrorEntity;
import org.dreamscale.exception.ErrorEntityWebApplicationExceptionFactory;
import org.dreamscale.exception.ResponseAdapter;

import java.io.IOException;
import java.util.Collection;

@Slf4j
public class WebApplicationExceptionErrorDecoder implements ErrorDecoder {

    private ObjectMapper objectMapper;
    private Decoder.Default defaultDecoder;
    private ErrorEntityWebApplicationExceptionFactory errorEntityWebApplicationExceptionFactory =
            new ErrorEntityWebApplicationExceptionFactory();

    public WebApplicationExceptionErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.defaultDecoder = new Decoder.Default();
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        return errorEntityWebApplicationExceptionFactory.createException(new FeignResponseAdapter(response, objectMapper, defaultDecoder));
    }

    @AllArgsConstructor
    private static class FeignResponseAdapter implements ResponseAdapter {

        private Response response;
        private ObjectMapper objectMapper;
        private Decoder.Default defaultDecoder;

        @Override
        public int getStatusCode() {
            return response.status();
        }

        @Override
        public Collection<String> getHeaders(String key) {
            return response.headers().get(key);
        }

        @Override
        public ErrorEntity getContentAsErrorEntity() throws IOException {
            byte[] bodyData = Util.toByteArray(response.body().asInputStream());
            return objectMapper.readerFor(ErrorEntity.class).readValue(bodyData);
        }

        @Override
        public String getContentAsString() throws IOException {
            return (String) defaultDecoder.decode(response, String.class);
        }

    }

}
