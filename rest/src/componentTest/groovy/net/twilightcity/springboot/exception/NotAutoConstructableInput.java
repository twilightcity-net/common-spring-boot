package net.twilightcity.springboot.exception;

public class NotAutoConstructableInput {

    private String value;

    public NotAutoConstructableInput(String value) {
        this.value = value;
    }

}
