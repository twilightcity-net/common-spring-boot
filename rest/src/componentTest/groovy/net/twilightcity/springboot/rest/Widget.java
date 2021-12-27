package net.twilightcity.springboot.rest;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@NoArgsConstructor
public class Widget {

    private Long id;
    private Duration duration;

    public Widget(Long id) {
        this.id = id;
    }

    public String toString() {
        return "Widget[${" + id + "}]";
    }

}
