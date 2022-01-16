package net.twilightcity.springboot.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.twilightcity.jackson.ObjectMapperBuilder;
import net.twilightcity.springboot.exception.ExceptionConfig;
import net.twilightcity.springboot.http.RequestContextFilter;
import net.twilightcity.springboot.logging.LoggingConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@Import({
        LoggingConfig.class,
        ExceptionConfig.class,
        CommonSpringBootAutoConfig.class
})
@Configuration
public class CommonSpringBootConfig {

    @Bean
    public RequestContextFilter commonRequestContextFilter() {
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
