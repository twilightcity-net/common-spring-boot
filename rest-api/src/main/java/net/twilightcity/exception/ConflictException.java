package net.twilightcity.exception;

import lombok.Getter;
import lombok.ToString;
import net.twilightcity.logging.LoggingLevel;
import net.twilightcity.http.HttpStatus;

@Getter
@ToString
public class ConflictException extends WebApplicationException {

    public ConflictException(ErrorCodes errorCode, String messageTemplate, Object... args) {
        this(null, errorCode, messageTemplate, args);
    }

    public ConflictException(Throwable cause, ErrorCodes errorCode, String messageTemplate, Object... args) {
        this(cause, LoggingLevel.WARN, errorCode, messageTemplate, args);
    }

    public ConflictException(Throwable cause, LoggingLevel logLevel, ErrorCodes errorCode, String messageTemplate, Object... args) {
        super(HttpStatus.SC_CONFLICT, ErrorEntity.create(logLevel, errorCode, messageTemplate, args), cause);
    }

    public ConflictException(ErrorEntity errorEntity) {
        super(HttpStatus.SC_CONFLICT, errorEntity);
    }

}
