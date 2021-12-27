package net.twilightcity.test;

import groovyx.net.http.RESTClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.net.URISyntaxException;

@Configuration
public class BaseTestConfig {

    @Value("http://localhost:${server.port}")
    protected String baseUrl;

    @Bean
    @Primary
    RESTClient restClient() throws URISyntaxException {
        return new RESTClient(baseUrl);
    }

}
