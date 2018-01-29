package org.dreamscale.springboot;

import org.springframework.boot.autoconfigure.security.SecurityProperties;

public interface FilterPrecedence {

    int FILTER_CHAIN_EXCEPTION_HANDLING_FILTER = Integer.MIN_VALUE;
    int REQUEST_CONTEXT_FILTER = FILTER_CHAIN_EXCEPTION_HANDLING_FILTER + 1;
    int MDC_CLEAR_FILTER = FILTER_CHAIN_EXCEPTION_HANDLING_FILTER + 2;

    int AUTHORIZATION = SecurityProperties.DEFAULT_FILTER_ORDER; //

    int LOGGING = AUTHORIZATION - 100;

    int MDC_POPULATING_FILTER = AUTHORIZATION + 100;

}
