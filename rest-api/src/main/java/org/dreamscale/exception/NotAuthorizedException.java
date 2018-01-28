package org.dreamscale.exception;

import lombok.Getter;
import lombok.ToString;
import org.dreamscale.http.HttpStatus;
import org.dreamscale.logging.LoggingLevel;

@Getter
@ToString
public class NotAuthorizedException extends WebApplicationException {

    public NotAuthorizedException(ErrorCodes errorCode, String messageTemplate, Object... args) {
        this(null, errorCode, messageTemplate, args);
    }

    public NotAuthorizedException(Throwable cause, ErrorCodes errorCode, String messageTemplate, Object... args) {
        this(cause, LoggingLevel.WARN, errorCode, messageTemplate, args);
    }

    public NotAuthorizedException(Throwable cause, LoggingLevel logLevel, ErrorCodes errorCode, String messageTemplate, Object... args) {
        super(HttpStatus.SC_UNAUTHORIZED, ErrorEntity.create(logLevel, errorCode, messageTemplate, args), cause);
    }

    public NotAuthorizedException(ErrorEntity errorEntity) {
        super(HttpStatus.SC_UNAUTHORIZED, errorEntity);
    }

}
