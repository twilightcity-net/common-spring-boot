package org.dreamscale.exception;

import org.dreamscale.http.HttpStatus;

public class SeeOtherException extends WebApplicationException {

    private String otherLocation;

    public SeeOtherException(String otherLocation) {
        this(null, otherLocation);
    }

    public SeeOtherException(Throwable cause, String otherLocation) {
        super(HttpStatus.SC_SEE_OTHER, ErrorEntity.builder().build(), cause);
        this.otherLocation = otherLocation;
    }

    public String getOtherLocation() {
        return otherLocation;
    }
}
