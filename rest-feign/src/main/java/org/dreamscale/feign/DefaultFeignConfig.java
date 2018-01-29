package org.dreamscale.feign;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import feign.Logger;
import org.dreamscale.jackson.ObjectMapperBuilder;
import org.dreamscale.logging.RequestResponseLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultFeignConfig {

    @Autowired
    private RequestResponseLoggerFactory requestResponseLoggerFactory;

    @Bean
    public JacksonFeignBuilder jacksonFeignBuilder() {
        ObjectMapperBuilder encoderObjectMapperBuilder = new ObjectMapperBuilder()
                .jsr310TimeModule()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ObjectMapperBuilder decoderObjectMapperBuilder = new ObjectMapperBuilder()
                .jsr310TimeModule()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        return new JacksonFeignBuilder()
                .logLevel(Logger.Level.FULL)
                .encoderObjectMapperBuilder(encoderObjectMapperBuilder)
                .decoderObjectMapperBuilder(decoderObjectMapperBuilder)
                .requestResponseLoggerFactory(requestResponseLoggerFactory)
                .requestInterceptor(new RequestAndSessionIdHeaderInterceptor());
    }

}
