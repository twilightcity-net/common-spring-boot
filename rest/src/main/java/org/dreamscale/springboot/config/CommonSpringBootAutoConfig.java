package org.dreamscale.springboot.config;


import org.dreamscale.logging.RequestResponseLoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

@ConditionalOnMissingBean(CommonSpringBootConfigurer.class)
public class CommonSpringBootAutoConfig {

    public CommonSpringBootConfigurer commonSpringBootConfigurer() {
        return new DefaultCommonSpringBootConfigurer();
    }

    public static class DefaultCommonSpringBootConfigurer implements CommonSpringBootConfigurer {

        public RequestResponseLoggerFactory configure(RequestResponseLoggerFactory requestResponseLoggerFactory) {
            return requestResponseLoggerFactory.excludeHeader("Authorization");
        }

    }

}
