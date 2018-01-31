package org.dreamscale.springboot.security;

import org.dreamscale.exception.ErrorCodes;

import static org.dreamscale.springboot.security.SecurityErrorCodeGroups.SECURITY;

public enum SecurityErrorCodes implements ErrorCodes {

    MISSING_OR_INVALID_AUTHORIZATION_TOKEN(1, SECURITY),
    EXPIRED_TOKEN(2, SECURITY),
    NOT_AUTHORIZED(3, SECURITY);

    private int subcode;
    private SecurityErrorCodeGroups errorCodeGroup;

    SecurityErrorCodes(int subcode, SecurityErrorCodeGroups errorCodeGroup) {
        this.subcode = subcode;
        this.errorCodeGroup = errorCodeGroup;
    }

    @Override
    public String makeErrorCode() {
        return this.errorCodeGroup.makeErrorCode(this.subcode);
    }

}

