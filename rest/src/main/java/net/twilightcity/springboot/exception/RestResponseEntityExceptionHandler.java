package net.twilightcity.springboot.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import net.twilightcity.exception.ErrorEntity;
import net.twilightcity.exception.ForbiddenException;
import net.twilightcity.exception.WebApplicationException;
import net.twilightcity.springboot.validation.ValidationErrorCodes;
import net.twilightcity.springboot.security.SecurityErrorCodes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.validation.ValidationException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String INTERNAL_SERVER_ERROR_MSG = "An internal server error occurred.";

    private ResponseEntity<Object> createResponseEntity(ErrorEntity errorEntity, HttpStatus status, WebRequest request) {
        return createResponseEntity(errorEntity, status.value(), request);
    }

    private ResponseEntity<Object> createResponseEntity(ErrorEntity errorEntity, int status, WebRequest request) {
        HttpMethod httpMethod = getHttpMethod(request);
        String[] headerValueArray = request.getHeaderValues(HttpHeaders.ACCEPT);
        Object body = getResponseBody(errorEntity, status, httpMethod, headerValueArray);
        return ResponseEntity.status(status)
                .body(body);
    }

    private HttpMethod getHttpMethod(WebRequest request) {
        return request instanceof ServletWebRequest ? ((ServletWebRequest) request).getHttpMethod() : null;
    }

    static Object getResponseBody(ErrorEntity errorEntity, int status, HttpMethod httpMethod, String[] acceptHeaders) {
        MediaTypeList mediaTypeList = new MediaTypeList(acceptHeaders);

        if (httpMethod != HttpMethod.HEAD) {
            if (mediaTypeList.isCompatibleWith(MediaType.APPLICATION_JSON)) {
                return errorEntity;
            } else if (mediaTypeList.isCompatibleWith(MediaType.TEXT_PLAIN)) {
                return errorEntity.getMessage();
                // internal errors will already have been logged
            } else if (status != HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                log.info("No supported media type, unable to return ErrorEntity={}", errorEntity);
            }
        }
        return null;
    }


    // this ensures all exceptions handled by the superclass will have an ErrorEntity body
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorEntity errorEntity = ErrorEntity.getOrCreate(ex);
        return createResponseEntity(errorEntity, status, request);
    }

    @ExceptionHandler(value = Throwable.class)
    protected ResponseEntity<Object> handleInternalError(Throwable ex, WebRequest request) {
        log.error(INTERNAL_SERVER_ERROR_MSG, ex);
        ErrorEntity errorEntity = ErrorEntity.builder()
                .message(INTERNAL_SERVER_ERROR_MSG)
                .build();
        return createResponseEntity(errorEntity, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {ForbiddenException.class, AccessDeniedException.class})
    protected ResponseEntity<Object> handleForbiddenException(Exception ex, WebRequest request) {
        ErrorEntity errorEntity = null;
        if (ex instanceof ForbiddenException) {
            errorEntity = ((ForbiddenException) ex).getErrorEntity();
        }
        if (errorEntity == null) {
            errorEntity = ErrorEntity.create(SecurityErrorCodes.NOT_AUTHORIZED, "Access is denied", new Object[0]);
        }
        return createResponseEntity(errorEntity, HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(value = InvalidFormatException.class)
    protected ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, WebRequest request) {
        ErrorEntity errorEntity = ErrorEntity.builder()
                .message(ex.getMessage())
                .violations(null)
                .errorCode(ValidationErrorCodes.ERROR_ENTITY_DATA_FORMAT.makeErrorCode())
                .build();
        return createResponseEntity(errorEntity, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException) {
            return handleInvalidFormatException((InvalidFormatException) ex.getCause(), request);
        } else if (cause instanceof JsonParseException || cause instanceof JsonMappingException) {
            return handleDataFormatExceptions((Exception) cause, request);
        } else {
            return super.handleHttpMessageNotReadable(ex, headers, status, request);
        }
    }

    @ExceptionHandler(value = {JsonMappingException.class, JsonParseException.class})
    protected ResponseEntity<Object> handleDataFormatExceptions(Exception ex, WebRequest request) {
        ErrorEntity errorEntity = ErrorEntity.builder()
                .message(ex.getMessage())
                .errorCode(ValidationErrorCodes.ERROR_ENTITY_DATA_FORMAT.makeErrorCode())
                .build();
        return createResponseEntity(errorEntity, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> violationsMap = new HashMap<>();
        if (ex.getBindingResult() != null) {
            for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
                violationsMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
        }
        // the default message of MethodArgumentNotValidException contains data we don't want to expose (like class names)
        // but it may be useful for debugging, so log it
        log.info("Invalid request argument(s), message={}", ex.getMessage());
        ErrorEntity errorEntity = ErrorEntity.builder()
                .message("One or more validation errors occurred")
                .violations(violationsMap)
                .errorCode(ValidationErrorCodes.ERROR_ENTITY_VALIDATION.makeErrorCode())
                .build();
        return createResponseEntity(errorEntity, status, request);
    }

    @ExceptionHandler(value = {ValidationException.class})
    protected ResponseEntity<Object> handleValidationExceptions(ValidationException ex, WebRequest request) {
        ErrorEntity errorEntity = ErrorEntity.builder()
                .message(ex.getMessage())
                .errorCode(ValidationErrorCodes.ERROR_ENTITY_VALIDATION.makeErrorCode())
                .build();
        return createResponseEntity(errorEntity, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = WebApplicationException.class)
    protected ResponseEntity<Object> handleWebApplicationException(WebApplicationException ex, WebRequest request) {
        int statusCode = ex.getStatusCode();
        ErrorEntity errorEntity = ErrorEntity.getOrCreate(ex);
        return createResponseEntity(errorEntity, statusCode, request);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        ErrorEntity errorEntity = ErrorEntity.builder()
                .message(ex.getMessage())
                .violations(getAllConstraintViolations(ex))
                .errorCode(ValidationErrorCodes.ERROR_ENTITY_VALIDATION.makeErrorCode())
                .build();
        return createResponseEntity(errorEntity, HttpStatus.BAD_REQUEST, request);
    }

    private Map<String, String> getAllConstraintViolations(ConstraintViolationException exception) {
        Map<String, String> violationsMap = new HashMap<>();
        Iterator sourceIterator = exception.getConstraintViolations().iterator();
        Iterable iterable = () -> sourceIterator;
        Stream<ConstraintViolation> violationStream = StreamSupport.stream(iterable.spliterator(), false);
        violationStream.forEach(violation -> {
            String invalidProperty = getLastNodeNameInPropertyPath(violation);
            if (invalidProperty != null) {
                addViolationMessageToMap(violationsMap, invalidProperty, violation);
            }
        });
        return violationsMap;
    }

    private String getLastNodeNameInPropertyPath(ConstraintViolation violation) {
        String nodeName = null;
        for (Path.Node node : violation.getPropertyPath()) {
            nodeName = node.getName();
        }
        return nodeName;
    }

    private void addViolationMessageToMap(Map<String, String> violationsMap, String invalidProperty, ConstraintViolation violation) {
        StringBuilder violationMessage = new StringBuilder();
        if (violationsMap.containsKey(invalidProperty)) {
            violationMessage.append(violationsMap.get(invalidProperty)).append(" ");
        }
        violationMessage.append(violation.getMessage());
        violationsMap.put(invalidProperty, violationMessage.toString());
    }

    private static class MediaTypeList {

        private List<MediaType> mediaTypes;

        MediaTypeList(String[] mediaTypeArray) {
            if (mediaTypeArray != null) {
                List<String> mediaTypeList = Arrays.asList(mediaTypeArray);
                mediaTypes = MediaType.parseMediaTypes(mediaTypeList);
            } else {
                mediaTypes = new ArrayList<>();
            }
        }

        boolean isCompatibleWith(MediaType mediaType) {
            return mediaTypes.stream()
                    .anyMatch(mt -> mt.isCompatibleWith(mediaType));
        }

    }

}
