package net.twilightcity.springboot.validation;


import net.twilightcity.exception.ErrorCodes;

import static net.twilightcity.springboot.validation.ValidationErrorCodeGroups.VALIDATION;

public enum ValidationErrorCodes implements ErrorCodes {
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
