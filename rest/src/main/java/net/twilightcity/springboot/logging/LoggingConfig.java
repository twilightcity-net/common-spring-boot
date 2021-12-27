package net.twilightcity.springboot.logging;

import net.twilightcity.logging.RequestResponseLoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {

    @Bean
    public LoggingFilter loggingFilter() {
        return new LoggingFilter();
    }

    @Bean
    public RequestResponseLoggerFactory requestResponseLoggerFactory() {
        return new RequestResponseLoggerFactory();
    }

    @Bean
    public MdcClearFilter mdcClearFilter() {
        return new MdcClearFilter();
    }

    @Bean
    public MdcPopulatingFilter mdcPopulatingFilter() {
        return new MdcPopulatingFilter();
    }

    @Bean
    public MdcSessionAndRequestPopulator mdcSessionAndRequestPopulator() {
        return new MdcSessionAndRequestPopulator();
    }

}
