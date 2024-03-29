package net.twilightcity.springboot.validation;


public enum ValidationErrorCodeGroups {

    VALIDATION("VAL");

    private String group;

    ValidationErrorCodeGroups(String group) {
        this.group = group;
    }

    public String makeErrorCode(int subcode) {
        return String.format("%s-%04d", this.group, subcode);
    }
}
