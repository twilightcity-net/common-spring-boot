package net.twilightcity.exception;

import lombok.Getter;
import lombok.ToString;
import net.twilightcity.http.HttpStatus;

@Getter
@ToString
public class WebApplicationException extends RuntimeException implements ErrorEntityException {

    public static int getStatusCode(Exception ex) {
        return (ex instanceof WebApplicationException) ? ((WebApplicationException) ex).getStatusCode() : HttpStatus.SC_INTERNAL_SERVER_ERROR;
    }


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
