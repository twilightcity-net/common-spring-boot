package net.twilightcity.springboot.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.twilightcity.exception.ErrorEntity;
import net.twilightcity.exception.WebApplicationException;
import net.twilightcity.springboot.FilterPrecedence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

/**
 * RestResponseEntityExceptionHandler is reponsible for handling exceptions thrown by Resource endpoints.  This class
 * is responsible for handling exceptions thrown by any filter further in the chain.
 */
@Slf4j
@Order(FilterPrecedence.FILTER_CHAIN_EXCEPTION_HANDLING_FILTER)
public class FilterExceptionHandlingRequestFilter extends OncePerRequestFilter {

    @Autowired
    ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            setResponseErrorEntityContent(request, response, ex);
        }
    }

    private void setResponseErrorEntityContent(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        try {
            ErrorEntity errorEntity = ErrorEntity.getOrCreate(ex);

            int statusCode = WebApplicationException.getStatusCode(ex);

            response.reset();
            response.setStatus(statusCode);

            HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());
            String[] acceptHeaderArray = getAcceptHeaders(request);
            Object responseBody = RestResponseEntityExceptionHandler.getResponseBody(errorEntity, response.getStatus(), httpMethod, acceptHeaderArray);
            if (responseBody instanceof ErrorEntity) {
                writeResponse(response, MediaType.APPLICATION_JSON_VALUE, responseBody);
            } else if (responseBody instanceof String) {
                writeResponse(response, MediaType.TEXT_PLAIN_VALUE, responseBody);
            } else {
                response.setContentLength(0);
            }
        } catch (Exception writeResponseException) {
            log.error("Failed to translate exception=" + ex + " to response", writeResponseException);
            log.info("Original exception stack trace", ex);
        }
    }

    private String[] getAcceptHeaders(HttpServletRequest request) {
        List<String> acceptHeaderList = Collections.list(request.getHeaders(HttpHeaders.ACCEPT));
        return acceptHeaderList.toArray(new String[acceptHeaderList.size()]);
    }

    private void writeResponse(HttpServletResponse response, String contentType, Object body) throws IOException {
        response.setContentType(contentType);
        PrintWriter out = response.getWriter();
        out.write(objectMapper.writeValueAsString(body));
        out.flush();
    }

}
