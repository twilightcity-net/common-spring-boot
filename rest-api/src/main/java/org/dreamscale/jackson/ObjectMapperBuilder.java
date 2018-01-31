package org.dreamscale.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.TimeZone;

public class ObjectMapperBuilder {

    private ObjectMapper mapper;

    public ObjectMapperBuilder() {
        mapper = new ObjectMapper();
    }

    private ObjectMapperBuilder(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public ObjectMapper build() {
        return copy();
    }

    private ObjectMapper copy() {
        return mapper.copy();
    }

    public ObjectMapperBuilder jsr310TimeModule() {
        ObjectMapper copy = copy()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .setTimeZone(TimeZone.getTimeZone("UTC"));
        return new ObjectMapperBuilder(copy);
    }

    public ObjectMapperBuilder setTimeZone(TimeZone tz) {
        ObjectMapper copy = copy().setTimeZone(tz);
        return new ObjectMapperBuilder(copy);
    }

    public ObjectMapperBuilder registerModule(Module module) {
        ObjectMapper copy = copy().registerModule(module);
        return new ObjectMapperBuilder(copy);
    }

    public ObjectMapperBuilder setPropertyNamingStrategy(PropertyNamingStrategy strategy) {
        ObjectMapper copy = copy().setPropertyNamingStrategy(strategy);
        return new ObjectMapperBuilder(copy);
    }

    public ObjectMapperBuilder setSerializationInclusion(JsonInclude.Include include) {
        ObjectMapper copy = copy().setSerializationInclusion(include);
        return new ObjectMapperBuilder(copy);
    }

    public ObjectMapperBuilder disable(SerializationFeature feature) {
        ObjectMapper copy = copy().disable(feature);
        return new ObjectMapperBuilder(copy);
    }

    public ObjectMapperBuilder disable(DeserializationFeature feature) {
        ObjectMapper copy = copy().disable(feature);
        return new ObjectMapperBuilder(copy);
    }

    public ObjectMapperBuilder disable(JsonParser.Feature... features) {
        ObjectMapper copy = copy().disable(features);
        return new ObjectMapperBuilder(copy);
    }

    public ObjectMapperBuilder disable(JsonGenerator.Feature... features) {
        ObjectMapper copy = copy().disable(features);
        return new ObjectMapperBuilder(copy);
    }

    public ObjectMapperBuilder disable(MapperFeature... features) {
        ObjectMapper copy = copy().disable(features);
        return new ObjectMapperBuilder(copy);
    }

    public ObjectMapperBuilder enable(SerializationFeature feature) {
        ObjectMapper copy = copy().enable(feature);
        return new ObjectMapperBuilder(copy);
    }

    public ObjectMapperBuilder enable(DeserializationFeature feature) {
        ObjectMapper copy = copy().enable(feature);
        return new ObjectMapperBuilder(copy);
    }

    public ObjectMapperBuilder enable(JsonParser.Feature... features) {
        ObjectMapper copy = copy().enable(features);
        return new ObjectMapperBuilder(copy);
    }

    public ObjectMapperBuilder enable(JsonGenerator.Feature... features) {
        ObjectMapper copy = copy().enable(features);
        return new ObjectMapperBuilder(copy);
    }

    public ObjectMapperBuilder enable(MapperFeature... features) {
        ObjectMapper copy = copy().enable(features);
        return new ObjectMapperBuilder(copy);
    }

}
