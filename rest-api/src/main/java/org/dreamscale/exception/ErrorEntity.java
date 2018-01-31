package org.dreamscale.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dreamscale.logging.LoggingLevel;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorEntity {
    private String errorCode;
    private Map<String,String> violations;
    private String message;
    private Object[] args;
    @JsonIgnore
    private LoggingLevel logLevel;

    public static ErrorEntity create(ErrorCodes errorCode, String messageTemplate, Object[] args) {
        return create(LoggingLevel.ERROR, errorCode, messageTemplate, args);
    }

    public static ErrorEntity create(LoggingLevel logLevel, ErrorCodes errorCode, String messageTemplate, Object[] args) {
        return ErrorEntity.builder()
                .errorCode(errorCode != null ? errorCode.makeErrorCode() : null)
                .message(String.format(messageTemplate, args))
                .logLevel(logLevel)
                .args(args)
                .build();
    }

    public static ErrorEntity getOrCreate(Exception ex) {
        ErrorEntity errorEntity = null;
        if (ex instanceof ErrorEntityException) {
            errorEntity = ((ErrorEntityException) ex).getErrorEntity();
        }

        if (errorEntity == null) {
            errorEntity = ErrorEntity.builder()
                    .message(ex.getMessage())
                    .build();
        }
        return errorEntity;
    }

}