package org.dreamscale.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class WebApplicationException extends RuntimeException implements ErrorEntityException {

    private ErrorEntity errorEntity;
    private int statusCode;

    public WebApplicationException(int statusCode, ErrorEntity errorEntity) {
        this(statusCode, errorEntity, null);
    }

    public WebApplicationException(int statusCode, ErrorEntity errorEntity, Throwable cause) {
        super(errorEntity.getMessage(), cause);
        this.statusCode = statusCode;
        this.errorEntity = errorEntity;
    }

}
