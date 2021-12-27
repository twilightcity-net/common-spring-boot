package net.twilightcity.logging;

public enum LoggingLevel {

    TRACE("trace"),
    DEBUG("debug"),
    WARN("warn"),
    ERROR("error"),
    NONE("none");

    private String name;

    LoggingLevel(java.lang.String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
