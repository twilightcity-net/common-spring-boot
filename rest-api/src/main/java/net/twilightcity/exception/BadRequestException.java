package net.twilightcity.exception;

import lombok.Getter;
import lombok.ToString;
import net.twilightcity.logging.LoggingLevel;
import net.twilightcity.http.HttpStatus;

@Getter
@ToString
public class BadRequestException extends WebApplicationException {

    public BadRequestException(ErrorCodes errorCode, String messageTemplate, Object... args) {
        this(null, errorCode, messageTemplate, args);
    }

    public BadRequestException(Throwable cause, ErrorCodes errorCode, String messageTemplate, Object... args) {
        this(cause, LoggingLevel.WARN, errorCode, messageTemplate, args);
    }

    public BadRequestException(Throwable cause, LoggingLevel logLevel, ErrorCodes errorCode, String messageTemplate, Object... args) {
        super(HttpStatus.SC_BAD_REQUEST, ErrorEntity.create(logLevel, errorCode, messageTemplate, args), cause);
    }

    public BadRequestException(ErrorEntity errorEntity) {
        super(HttpStatus.SC_BAD_REQUEST, errorEntity);
    }

}
