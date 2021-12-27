package net.twilightcity.springboot.exception;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExceptionConfig {

    @Bean
    public FilterExceptionHandlingRequestFilter filterExceptionHandlingRequestFilter() {
        return new FilterExceptionHandlingRequestFilter();
    }

    @Bean
    public RestResponseEntityExceptionHandler restResponseEntityExceptionHandler() {
        return new RestResponseEntityExceptionHandler();
    }

}
