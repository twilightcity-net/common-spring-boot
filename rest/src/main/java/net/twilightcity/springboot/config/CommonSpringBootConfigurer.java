package net.twilightcity.springboot.config;

import net.twilightcity.logging.RequestResponseLoggerFactory;

public interface CommonSpringBootConfigurer {

    /**
     * RequestResponseLoggerFactory is immutable, so the configured object is returned.
     */
    RequestResponseLoggerFactory configure(RequestResponseLoggerFactory requestResponseLoggerFactory);

}
