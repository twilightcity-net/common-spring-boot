package org.dreamscale.feign;

import com.google.common.net.MediaType;
import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import feign.codec.StringDecoder;
import feign.jackson.JacksonDecoder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;

public class JsonOrTextDecoder implements Decoder {

    private static final MediaType PLAIN_TEXT = MediaType.PLAIN_TEXT_UTF_8.withoutParameters();

    JacksonDecoder jacksonDecoder;
    StringDecoder stringDecoder;

    public JsonOrTextDecoder(JacksonDecoder jacksonDecoder) {
        this.jacksonDecoder = jacksonDecoder;
        this.stringDecoder = new StringDecoder();
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        if (isMediaType(response, PLAIN_TEXT)) {
            return stringDecoder.decode(response, type);
        } else {
            return jacksonDecoder.decode(response, type);
        }
    }

    private boolean isMediaType(Response response, MediaType expectedMediaType) {
        Collection<String> contentTypeHeaders = response.headers().get("content-type");
        if (contentTypeHeaders != null) {
            for (String header : contentTypeHeaders) {
                MediaType actualMediaType = MediaType.parse(header).withoutParameters();
                if (expectedMediaType.is(actualMediaType)) {
                    return true;
                }
            }
        }
        return false;
    }

}
