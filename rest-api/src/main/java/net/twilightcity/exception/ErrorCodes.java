package net.twilightcity.exception;

import com.fasterxml.jackson.annotation.JsonValue;

public interface ErrorCodes {

    @JsonValue
    String makeErrorCode();
}
