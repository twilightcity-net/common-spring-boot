package net.twilightcity.feign;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.jackson.JacksonEncoder;

import java.lang.reflect.Type;

public class JsonOrTextEncoder implements Encoder {

    private JacksonEncoder jacksonEncoder;
    private Default defaultEncoder;

    public JsonOrTextEncoder(JacksonEncoder jacksonEncoder) {
        this.jacksonEncoder = jacksonEncoder;
        this.defaultEncoder = new Default();
    }

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
        if (bodyType == String.class) {
            defaultEncoder.encode(object, bodyType, template);
        } else {
            jacksonEncoder.encode(object, bodyType, template);
        }
    }

}
