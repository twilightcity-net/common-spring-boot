package net.twilightcity.springboot.exception;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ValidatableEnum {
    VAL("value");

    private final String val;

    ValidatableEnum(String val) {
        this.val = val;
    }

    @JsonValue
    String getValue() {
        return val;
    }

}
