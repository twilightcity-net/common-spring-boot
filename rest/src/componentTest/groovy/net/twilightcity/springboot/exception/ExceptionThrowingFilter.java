package net.twilightcity.springboot.exception;

import net.twilightcity.exception.ErrorEntity;
import net.twilightcity.exception.WebApplicationException;
import net.twilightcity.http.HttpStatus;
import net.twilightcity.springboot.FilterPrecedence;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(FilterPrecedence.FILTER_CHAIN_EXCEPTION_HANDLING_FILTER + 1)
public class ExceptionThrowingFilter extends OncePerRequestFilter {

    public static final String FAILURE_PATH = "/exceptionThrowingFilter/expectationFailed";
    public static final String FAILURE_MESSAGE = "So very not expected";

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (FAILURE_PATH.equals(httpServletRequest.getServletPath())) {
            ErrorEntity errorEntity = ErrorEntity.builder()
                    .message(FAILURE_MESSAGE)
                    .build();
            throw new WebApplicationException(HttpStatus.SC_EXPECTATION_FAILED, errorEntity);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}
