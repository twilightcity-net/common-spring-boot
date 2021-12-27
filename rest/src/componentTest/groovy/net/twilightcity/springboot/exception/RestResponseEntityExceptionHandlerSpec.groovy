package net.twilightcity.springboot.exception

import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient
import net.twilightcity.ComponentTest
import net.twilightcity.exception.ErrorCodes
import net.twilightcity.http.HttpStatus
import net.twilightcity.springboot.validation.ValidationErrorCodes
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

@ComponentTest
class RestResponseEntityExceptionHandlerSpec extends Specification {

    @Autowired
    private RESTClient client

    private void hitExceptionEndpoint(String path) {
        client.get(
                path: path,
                requestContentType: 'application/json'
        )
    }

    def "should respond with ERROR_ENTITY_DATA_FORMAT when when an JsonMappingException is thrown"() {
        when:
        hitExceptionEndpoint('/exception/jsonMappingException')

        then:
        def exception = thrown(HttpResponseException)
        assertResponse(exception, ValidationErrorCodes.ERROR_ENTITY_DATA_FORMAT, HttpStatus.SC_BAD_REQUEST, "json mapping exception")
    }

    def "should respond with ERROR_ENTITY_DATA_FORMAT if there is an issue constructing the object from json (JsonMappingException thrown)"() {
        when:
        client.post(
                path: '/exception/notAutoConstructableEntity',
                requestContentType: 'application/json',
                body: '{"value":"something"}'
        )

        then:
        def exception = thrown(HttpResponseException)
        assertResponse(exception, ValidationErrorCodes.ERROR_ENTITY_DATA_FORMAT, HttpStatus.SC_BAD_REQUEST, "Cannot construct instance of")
    }

    def "should respond with ERROR_ENTITY_DATA_FORMAT when an JsonParseException is thrown"() {
        when:
        hitExceptionEndpoint('/exception/jsonParseException')

        then:
        def exception = thrown(HttpResponseException)
        assertResponse(exception, ValidationErrorCodes.ERROR_ENTITY_DATA_FORMAT, HttpStatus.SC_BAD_REQUEST, "json parse exception")
    }

    def "should respond with ERROR_ENTITY_DATA_FORMAT if poorly formed json is received (JsonParseException thrown)"() {
        when:
        client.post(
                path: '/exception/validateInputEntity',
                requestContentType: 'application/json',
                body: '{"value":"something", "enumVal"}'
        )

        then:
        def exception = thrown(HttpResponseException)
        assertResponse(exception, ValidationErrorCodes.ERROR_ENTITY_DATA_FORMAT, HttpStatus.SC_BAD_REQUEST, "Unexpected character ('}'")
    }

    def "should respond with ERROR_ENTITY_DATA_FORMAT if an enum cannot be deserialized"() {
        when:
        client.post([
                path              : '/exception/validateInputEntity',
                requestContentType: 'application/json',
                body              : [
                        value  : "something",
                        enumVal: "not-value"
                ]
        ])

        then:
        def exception = thrown(HttpResponseException)
        assertResponse(exception, ValidationErrorCodes.ERROR_ENTITY_DATA_FORMAT, HttpStatus.SC_BAD_REQUEST, "Cannot deserialize value of type `net.twilightcity.springboot.exception.ValidatableEnum`")
    }

    def "should respond with ERROR_ENTITY_VALIDATION when an ValidationException is thrown"() {
        when:
        hitExceptionEndpoint('/exception/validationException')

        then:
        def exception = thrown(HttpResponseException)
        assertResponse(exception, ValidationErrorCodes.ERROR_ENTITY_VALIDATION, HttpStatus.SC_BAD_REQUEST, "mappers exception")
    }

    def "should respond with ERROR_ENTITY_VALIDATION if an invalid request is received"() {
        when:
        client.post(
                path: '/exception/validateInputEntity',
                requestContentType: 'application/json',
                body: '{}'
        )

        then:
        HttpResponseException ex = thrown(HttpResponseException)
        assertResponse(ex, ValidationErrorCodes.ERROR_ENTITY_VALIDATION, HttpStatus.SC_BAD_REQUEST, "One or more validation errors occurred")

        String errorResponseData = ex.getResponse().getData()
        assert errorResponseData.contains(/violations={value=must not be null}/)
    }

    def "should respond with custom error code when an exception is thrown with embedded ErrorEntity"() {
        when:
        hitExceptionEndpoint('/exception/badRequestException')

        then:
        HttpResponseException ex = thrown(HttpResponseException)
        assertResponse(ex, new TestErrorCode(), HttpStatus.SC_BAD_REQUEST, "Test Error")
    }

    def "should convert exception handled by RestResponseEntityExceptionHandler superclass"() {
        when:
        hitExceptionEndpoint('/exception/httpMediaTypeNotAcceptableException')

        then:
        HttpResponseException ex = thrown(HttpResponseException)
        assertResponse(ex, HttpStatus.SC_NOT_ACCEPTABLE, "try again")
    }

    def "should respond with no content on HEAD request failure"() {
        when:
        client.head(
                path: "/exception/notFoundExceptionWithMessage",
                requestContentType: 'application/json'
        )

        then:
        def ex = thrown(HttpResponseException)
        assert ex.getResponse().status == HttpStatus.SC_NOT_FOUND
        assert ex.getResponse().getData() == null
    }

    private static String assertResponse(HttpResponseException e, ErrorCodes errorCode = null,
                                         int statusCode, String message) {
        String errorResponseData = e.getResponse().getData()
        assert e.statusCode == statusCode
        if (errorCode != null) {
            assert errorResponseData.contains(/errorCode=${errorCode?.makeErrorCode()}/)
        }
        assert errorResponseData.contains(/message=${message}/)
        errorResponseData
    }

}
