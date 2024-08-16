package net.twilightcity.springboot.logging;

import net.twilightcity.logging.LoggingContext;
import net.twilightcity.springboot.FilterPrecedence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Order(FilterPrecedence.MDC_POPULATING_FILTER)
public class MdcPopulatingFilter extends OncePerRequestFilter {

    @Autowired
    private List<MdcPopulator> mdcPopulators;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try (LoggingContext c = buildLoggingContext(httpServletRequest)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

    private LoggingContext buildLoggingContext(HttpServletRequest httpServletRequest) {
        LoggingContext.Builder builder = new LoggingContext.Builder();

        for (MdcPopulator mdcPopulator : mdcPopulators) {
            mdcPopulator.populate(httpServletRequest, builder);
        }

        return builder.build();
    }

}
