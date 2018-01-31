package org.dreamscale.springboot.exception;

import org.dreamscale.exception.ErrorCodes;

public class TestErrorCode implements ErrorCodes {
    @Override
    public String makeErrorCode() {
        return "TEST-001";
    }
}
