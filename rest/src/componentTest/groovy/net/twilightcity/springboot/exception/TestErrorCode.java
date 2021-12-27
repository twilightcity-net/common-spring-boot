package net.twilightcity.springboot.exception;

import net.twilightcity.exception.ErrorCodes;

public class TestErrorCode implements ErrorCodes {
    @Override
    public String makeErrorCode() {
        return "TEST-001";
    }
}
