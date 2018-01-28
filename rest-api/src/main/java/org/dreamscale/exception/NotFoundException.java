package org.dreamscale.exception;

import lombok.Getter;
import lombok.ToString;
import org.dreamscale.http.HttpStatus;
import org.dreamscale.logging.LoggingLevel;

@Getter
@ToString
public class NotFoundException extends WebApplicationException {

    public NotFoundException(String messageTemplate, Object... args) {
        this(null, messageTemplate, args);
    }

    public NotFoundException(Throwable cause, String messageTemplate, Object... args) {
        this(cause, LoggingLevel.WARN, messageTemplate, args);
    }

    public NotFoundException(Throwable cause, LoggingLevel logLevel, String messageTemplate, Object... args) {
        this(cause, logLevel, null, messageTemplate, args);
    }

    public NotFoundException(Throwable cause, LoggingLevel logLevel, ErrorCodes errorCode, String messageTemplate, Object... args) {
        super(HttpStatus.SC_NOT_FOUND, ErrorEntity.create(logLevel, errorCode, messageTemplate, args), cause);
    }

    public NotFoundException(ErrorEntity errorEntity) {
        super(HttpStatus.SC_NOT_FOUND, errorEntity);
    }

}
