package org.dreamscale.springboot.logging;

import lombok.extern.slf4j.Slf4j;
import org.dreamscale.springboot.FilterPrecedence;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Order(FilterPrecedence.MDC_CLEAR_FILTER)
public class MdcClearFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        MDC.clear();
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
