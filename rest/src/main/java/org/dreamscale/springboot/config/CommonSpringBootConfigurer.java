package org.dreamscale.springboot.config;

import org.dreamscale.logging.RequestResponseLoggerFactory;

public interface CommonSpringBootConfigurer {

    RequestResponseLoggerFactory configure(RequestResponseLoggerFactory requestResponseLoggerFactory);

}
