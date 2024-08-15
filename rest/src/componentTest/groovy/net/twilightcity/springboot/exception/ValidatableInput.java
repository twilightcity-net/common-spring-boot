package net.twilightcity.springboot.exception;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class ValidatableInput {
    @NotNull
    private String value;
    private ValidatableEnum enumVal;
}
