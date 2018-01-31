package org.dreamscale.springboot.config;

import org.dreamscale.springboot.exception.ExceptionConfig;
import org.dreamscale.springboot.http.RequestContextFilter;
import org.dreamscale.springboot.logging.LoggingConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
        LoggingConfig.class,
        ExceptionConfig.class,
        CommonSpringBootAutoConfig.class
})
@Configuration
public class CommonSpringBootConfig {

    @Bean
    public RequestContextFilter requestContextFilter() {
        return new RequestContextFilter();
    }

}
