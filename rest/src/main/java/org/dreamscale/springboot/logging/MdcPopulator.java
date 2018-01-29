package org.dreamscale.springboot.logging;

import org.dreamscale.logging.LoggingContext;

import javax.servlet.http.HttpServletRequest;

public interface MdcPopulator {

    void populate(HttpServletRequest request, LoggingContext.Builder contextBuilder);

}
