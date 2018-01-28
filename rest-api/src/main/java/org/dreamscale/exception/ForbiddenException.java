package org.dreamscale.exception;

import lombok.Getter;
import lombok.ToString;
import org.dreamscale.http.HttpStatus;
import org.dreamscale.logging.LoggingLevel;

@Getter
@ToString
public class ForbiddenException extends WebApplicationException {

    public ForbiddenException(ErrorCodes errorCode, String messageTemplate, Object... args) {
        this(null, errorCode, messageTemplate, args);
    }

    public ForbiddenException(Throwable cause, ErrorCodes errorCode, String messageTemplate, Object... args) {
        this(cause, LoggingLevel.WARN, errorCode, messageTemplate, args);
    }

    public ForbiddenException(Throwable cause, LoggingLevel logLevel, ErrorCodes errorCode, String messageTemplate, Object... args) {
        super(HttpStatus.SC_FORBIDDEN, ErrorEntity.create(logLevel, errorCode, messageTemplate, args), cause);
    }

    public ForbiddenException(ErrorEntity errorEntity) {
        super(HttpStatus.SC_FORBIDDEN, errorEntity);
    }

}
