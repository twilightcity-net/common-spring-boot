package org.dreamscale.springboot.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dreamscale.jackson.ObjectMapperBuilder;
import org.dreamscale.springboot.exception.ExceptionConfig;
import org.dreamscale.springboot.http.RequestContextFilter;
import org.dreamscale.springboot.logging.LoggingConfig;
import org.dreamscale.springboot.swagger.SwaggerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@Import({
        LoggingConfig.class,
        ExceptionConfig.class,
        SwaggerConfig.class,
        CommonSpringBootAutoConfig.class
})
@Configuration
public class CommonSpringBootConfig {

    @Bean
    public RequestContextFilter requestContextFilter() {
        return new RequestContextFilter();
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapperBuilder()
                .jsr310TimeModule()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .build();
    }

}
