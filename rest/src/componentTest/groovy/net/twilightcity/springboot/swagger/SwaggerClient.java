package net.twilightcity.springboot.swagger;

import feign.Headers;
import feign.RequestLine;

import java.util.Map;

@Headers({
        "Content-Type: application/json",
        "Accept: application/json",
})
public interface SwaggerClient {

    @RequestLine("GET /v3/api-docs")
    Map getSwaggerJson();

}
