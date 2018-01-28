package org.dreamscale.exception;

import lombok.Getter;
import lombok.ToString;
import org.dreamscale.http.HttpStatus;
import org.dreamscale.logging.LoggingLevel;

@Getter
@ToString
public class InternalServerException extends WebApplicationException {

    public InternalServerException(ErrorCodes errorCode, String messageTemplate, Object... args) {
        this(null, errorCode, messageTemplate, args);
    }

    public InternalServerException(Throwable cause, ErrorCodes errorCode, String messageTemplate, Object... args) {
        this(cause, LoggingLevel.ERROR, errorCode, messageTemplate, args);
    }

    public InternalServerException(Throwable cause, LoggingLevel logLevel, ErrorCodes errorCode, String messageTemplate, Object... args) {
        super(HttpStatus.SC_INTERNAL_SERVER_ERROR, ErrorEntity.create(logLevel, errorCode, messageTemplate, args), cause);
    }

    public InternalServerException(ErrorEntity errorEntity) {
        super(HttpStatus.SC_INTERNAL_SERVER_ERROR, errorEntity);
    }

}
