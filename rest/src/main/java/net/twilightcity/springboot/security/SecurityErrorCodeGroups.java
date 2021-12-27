package net.twilightcity.springboot.security;

public enum SecurityErrorCodeGroups {

    SECURITY("SEC");

    private String group;

    SecurityErrorCodeGroups(String group) {
        this.group = group;
    }

    public String makeErrorCode(int subcode) {
        return String.format("%s-%04d", this.group, subcode);
    }

}
