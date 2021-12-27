package net.twilightcity.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.InvocationHandlerFactory;
import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import feign.ResponseMapper;
import feign.Retryer;
import feign.Target;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.FormEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import net.twilightcity.jackson.ObjectMapperBuilder;
import net.twilightcity.logging.RequestResponseLoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Builder(toBuilder = true)
@AllArgsConstructor
public class JacksonFeignBuilder {

    private RequestResponseLoggerFactory requestResponseLoggerFactory;
    private ObjectMapperBuilder encoderObjectMapperBuilder;
    private ObjectMapperBuilder decoderObjectMapperBuilder;
    private ResponseMapper responseMapper;
    private ErrorDecoder errorDecoder;
    private Class apiType;
    private String baseUrl;
    private boolean formEncodingEnabled;

    private Logger.Level logLevel;
    private Contract contract;
    private Client client;
    private Retryer retryer;
    private Logger logger;
    private Boolean decode404;
    private Request.Options options;
    @Singular
    private List<RequestInterceptor> requestInterceptors;
    private InvocationHandlerFactory invocationHandlerFactory;

    public JacksonFeignBuilder() {
        requestInterceptors = new ArrayList<>();
        encoderObjectMapperBuilder = new ObjectMapperBuilder();
        decoderObjectMapperBuilder = new ObjectMapperBuilder();
    }

    public JacksonFeignBuilder requestResponseLoggerFactory(RequestResponseLoggerFactory requestResponseLoggerFactory) {
        return toBuilder()
                .requestResponseLoggerFactory(requestResponseLoggerFactory)
                .build();
    }

    public JacksonFeignBuilder encoderObjectMapperBuilder(ObjectMapperBuilder encoderObjectMapperBuilder) {
        return toBuilder()
                .encoderObjectMapperBuilder(encoderObjectMapperBuilder)
                .build();
    }

    public JacksonFeignBuilder decoderObjectMapperBuilder(ObjectMapperBuilder decoderObjectMapperBuilder) {
        return toBuilder()
                .decoderObjectMapperBuilder(decoderObjectMapperBuilder)
                .build();
    }

    public JacksonFeignBuilder propertyNamingStrategy(PropertyNamingStrategy propertyNamingStrategy) {
        return toBuilder()
                .encoderObjectMapperBuilder(encoderObjectMapperBuilder.setPropertyNamingStrategy(propertyNamingStrategy))
                .decoderObjectMapperBuilder(decoderObjectMapperBuilder.setPropertyNamingStrategy(propertyNamingStrategy))
                .build();
    }

    public JacksonFeignBuilder baseUrl(String baseUrl) {
        return toBuilder()
                .baseUrl(baseUrl)
                .build();
    }

    public JacksonFeignBuilder apiType(Class apiType) {
        return toBuilder()
                .apiType(apiType)
                .build();
    }

    public JacksonFeignBuilder logLevel(Logger.Level logLevel) {
        return toBuilder()
                .logLevel(logLevel)
                .build();
    }

    public JacksonFeignBuilder contract(Contract contract) {
        return toBuilder()
                .contract(contract)
                .build();
    }

    public JacksonFeignBuilder client(Client client) {
        return toBuilder()
                .client(client)
                .build();
    }

    public JacksonFeignBuilder retryer(Retryer retryer) {
        return toBuilder()
                .retryer(retryer)
                .build();
    }

    public JacksonFeignBuilder logger(Logger logger) {
        return toBuilder()
                .logger(logger)
                .build();
    }

    public JacksonFeignBuilder responseMapper(ResponseMapper responseMapper) {
        return toBuilder()
                .responseMapper(responseMapper)
                .build();
    }

    public JacksonFeignBuilder decode404() {
        return toBuilder()
                .decode404(true)
                .build();
    }

    public JacksonFeignBuilder errorDecoder(ErrorDecoder errorDecoder) {
        return toBuilder()
                .errorDecoder(errorDecoder)
                .build();
    }

    public JacksonFeignBuilder options(Request.Options options) {
        return toBuilder()
                .options(options)
                .build();
    }

    public JacksonFeignBuilder requestInterceptor(RequestInterceptor requestInterceptor) {
        return toBuilder()
                .requestInterceptor(requestInterceptor)
                .build();
    }

    public JacksonFeignBuilder requestInterceptors(List<RequestInterceptor> requestInterceptors) {
        return toBuilder()
                .requestInterceptors(requestInterceptors)
                .build();
    }

    public JacksonFeignBuilder clearRequestInterceptors() {
        return toBuilder()
                .clearRequestInterceptors()
                .build();
    }

    public JacksonFeignBuilder invocationHandlerFactory(InvocationHandlerFactory invocationHandlerFactory) {
        return toBuilder()
                .invocationHandlerFactory(invocationHandlerFactory)
                .build();
    }

    /**
     * feign only supports single inheritance and only one level of inheritance, meaning you only get a single
     * interface to extend from.  using the tag interface is most convenient b/c other services which construct
     * the client don't need any additional information.  however, if for some reason that is not a viable option,
     * use this to enable multipart/form-data support on the client being built.
     */
    public JacksonFeignBuilder enableFormEncoding() {
        formEncodingEnabled = true;
        return this;
    }

    private Feign.Builder createBuilder(Class targetType) {
        Feign.Builder builder = new Feign.Builder();

        if (contract != null) {
            builder.contract(contract);
        }
        if (client != null) {
            builder.client(client);
        }
        if (retryer != null) {
            builder.retryer(retryer);
        }
        if (decode404 != null) {
            builder.decode404();
        }
        if (options != null) {
            builder.options(options);
        }
        if (requestInterceptors != null) {
            builder.requestInterceptors(requestInterceptors);
        }
        if (invocationHandlerFactory != null) {
            builder.invocationHandlerFactory(invocationHandlerFactory);
        }
        setLogger(builder, targetType);
        setEncoderAndDecoder(builder, targetType);
        return builder;
    }

    private void setLogger(Feign.Builder builder, Class apiType) {
        Logger logger = this.logger;
        if (logger == null && requestResponseLoggerFactory != null) {
            logger = new FeignLogger(requestResponseLoggerFactory, apiType);
        }
        if (logger != null) {
            builder.logger(logger);
            if (logLevel != null) {
                builder.logLevel(logLevel);
            }
        }
    }

    private void setEncoderAndDecoder(Feign.Builder builder, Class<?> apiType) {
        JacksonEncoder jacksonEncoder = new JacksonEncoder(encoderObjectMapperBuilder.build());
        Encoder encoder = new JsonOrTextEncoder(jacksonEncoder);
        if (formEncodingEnabled || FormUploadClient.class.isAssignableFrom(apiType)) {
            encoder = new FormEncoder(encoder);
        }

        builder.encoder(encoder);

        ObjectMapper responseObjectMapper = decoderObjectMapperBuilder.build();
        JsonOrTextDecoder decoder = new JsonOrTextDecoder(new JacksonDecoder(responseObjectMapper));
        if (responseMapper == null) {
            builder.decoder(decoder);
        } else {
            builder.mapAndDecode(responseMapper, decoder);
        }

        ErrorDecoder errorDecoder = this.errorDecoder;
        if (errorDecoder == null) {
            errorDecoder = new WebApplicationExceptionErrorDecoder(responseObjectMapper);
        }
        builder.errorDecoder(errorDecoder);
    }

    public <T> T target() {
        return target((Class<T>) apiType, baseUrl);
    }

    public <T> T target(Class<T> apiType) {
        return createBuilder(apiType).target(Target.EmptyTarget.create(apiType));
    }

    public <T> T target(Class<T> apiType, String baseUrl) {
        return createBuilder(apiType).target(apiType, baseUrl);
    }

}
