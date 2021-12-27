package net.twilightcity.transaction;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactorConfig {

    @Bean
    MandatoryTransactor mandatoryTransactor() {
        return new MandatoryTransactor();
    }

    @Bean
    RequiredTransactor requiredTransactor() {
        return new RequiredTransactor();
    }

    @Bean
    RequiresNewTransactor requiresNewTransactor() {
        return new RequiresNewTransactor();
    }

    @Bean
    SupportsTransactor supportsTransactor() {
        return new SupportsTransactor();
    }

}
