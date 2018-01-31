package org.dreamscale.springboot.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Widget {

    private Long id = 5L;

    public String toString() {
        return "Widget[${" + id + "}]";
    }

}
