package org.dreamscale.springboot.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.dreamscale.exception.WebApplicationException;
import org.dreamscale.http.HttpStatus;
import org.dreamscale.logging.LoggerSupport;
import org.dreamscale.logging.RequestResponseLoggerFactory;
import org.dreamscale.springboot.FilterPrecedence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@ControllerAdvice
@Order(FilterPrecedence.LOGGING)
public class LoggingFilter extends OncePerRequestFilter implements ResponseBodyAdvice {

    private static final ThreadLocal<Object> RESPONSE_BODY = new ThreadLocal<>();

    @Value("${rest.logging.prefix:rest.invocation}")
    private String pathPrefix;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    private RequestResponseLoggerFactory requestResponseLoggerFactory;

    @Autowired
    public void setRequestResponseLoggerFactory(RequestResponseLoggerFactory requestResponseLoggerFactory) {
        this.requestResponseLoggerFactory = requestResponseLoggerFactory.id("Server");
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        RESPONSE_BODY.set(o);
        return o;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ServletRequestAdapter servletRequestAdapter = new ServletRequestAdapter(request);
        Logger logger = getLogger(servletRequestAdapter);

        try {
            requestResponseLoggerFactory.createRequestLogger(servletRequestAdapter, logger).logRequest();
            try {
                filterChain.doFilter(servletRequestAdapter.getRequest(), response);
            } catch (IOException | RuntimeException ex) {
                logResponseFilterChainFailure(request, ex);
                throw ex;
            }
            Object responseBody = RESPONSE_BODY.get();

            ServletResponseAdapter responseAdapter = new ServletResponseAdapter(request, response, objectMapper, responseBody);
            requestResponseLoggerFactory.createResponseLogger(responseAdapter, logger).logResponse();
        } finally {
            RESPONSE_BODY.remove();
        }
    }

    public void logResponseFilterChainFailure(HttpServletRequest request, Exception failure) {
        if (log.isInfoEnabled()) {
            log.info(getResponseInfo(request, failure));
        }
    }

    private String getResponseInfo(HttpServletRequest request, Exception ex) {
        int statusCode = (ex instanceof WebApplicationException) ? ((WebApplicationException) ex).getStatusCode() : HttpStatus.SC_INTERNAL_SERVER_ERROR;
        return "Server response: [" + request.getMethod() + " " + request.getRequestURI() + " < " + statusCode + "]";
    }

    private Logger getLogger(ServletRequestAdapter requestAdapter) {
        String pathTemplateString = getPathTemplateString(requestAdapter.getRequest());
        String path = pathPrefix;
        if (pathTemplateString.startsWith(".") == false) {
            path += ".";
        }
        path += pathTemplateString;
        return LoggerFactory.getLogger(path);
    }

    private String getPathTemplateString(HttpServletRequest request) {
        try {
            // invoking this method will initialize the HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE attribute
            requestMappingHandlerMapping.getHandler(request);
        } catch (Exception ex) {
            log.trace("Failed to resolve handler", ex);
        }
        Object uriTemplate = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        return uriTemplate == null ? "unknown" : uriTemplate.toString().replaceAll("/", ".");
    }

}
