package net.twilightcity.springboot.security;

import net.twilightcity.exception.ErrorCodes;

public enum SecurityErrorCodes implements ErrorCodes {

    MISSING_OR_INVALID_AUTHORIZATION_TOKEN(1, SecurityErrorCodeGroups.SECURITY),
    EXPIRED_TOKEN(2, SecurityErrorCodeGroups.SECURITY),
    NOT_AUTHORIZED(3, SecurityErrorCodeGroups.SECURITY);

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

