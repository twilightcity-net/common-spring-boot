package org.dreamscale.springboot.validation;


import org.dreamscale.exception.ErrorCodes;

import static org.dreamscale.springboot.validation.ValidationErrorCodeGroups.VALIDATION;

public enum  ValidationErrorCodes implements ErrorCodes {
    ERROR_ENTITY_VALIDATION(1, VALIDATION),
    ERROR_ENTITY_DATA_FORMAT(2, VALIDATION);

    private int subcode;
    private ValidationErrorCodeGroups errorCodeGroup;

    ValidationErrorCodes(int subcode, ValidationErrorCodeGroups errorCodeGroups) {
        this.subcode = subcode;
        this.errorCodeGroup = errorCodeGroups;
    }

    @Override
    public String makeErrorCode() {
        return this.errorCodeGroup.makeErrorCode(this.subcode);
    }

}
