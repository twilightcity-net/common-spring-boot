package org.dreamscale.springboot.config;

import org.dreamscale.logging.RequestResponseLoggerFactory;

public interface CommonSpringBootConfigurer {

    /**
     * RequestResponseLoggerFactory is immutable, so the configured object is returned.
     */
    RequestResponseLoggerFactory configure(RequestResponseLoggerFactory requestResponseLoggerFactory);

}
