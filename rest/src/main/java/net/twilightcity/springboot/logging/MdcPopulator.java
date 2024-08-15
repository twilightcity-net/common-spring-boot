package net.twilightcity.springboot.logging;

import net.twilightcity.logging.LoggingContext;

import jakarta.servlet.http.HttpServletRequest;

public interface MdcPopulator {

    void populate(HttpServletRequest request, LoggingContext.Builder contextBuilder);

}
