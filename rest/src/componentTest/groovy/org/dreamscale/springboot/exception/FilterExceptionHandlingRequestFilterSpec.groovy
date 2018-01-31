package org.dreamscale.springboot.exception

import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient
import org.dreamscale.ComponentTest
import org.dreamscale.http.HttpStatus
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

@ComponentTest
class FilterExceptionHandlingRequestFilterSpec extends Specification {

    @Autowired
    private RESTClient client

    def "should translate an exception generated from a filter into an ErrorEntity"() {
        when:
        client.get(
                path: ExceptionThrowingFilter.FAILURE_PATH,
                requestContentType: 'application/json'
        )

        then:
        def exception = thrown(HttpResponseException)
        assertResponse(exception, HttpStatus.SC_EXPECTATION_FAILED, ExceptionThrowingFilter.FAILURE_MESSAGE)
    }

    def "should respond with no content on HEAD request failure"() {
        when:
        client.head(
                path: ExceptionThrowingFilter.FAILURE_PATH,
                requestContentType: 'application/json'
        )

        then:
        def ex = thrown(HttpResponseException)
        assert ex.getResponse().status == HttpStatus.SC_EXPECTATION_FAILED
        assert ex.getResponse().getData() == null
    }

    private static String assertResponse(HttpResponseException e, int statusCode, String message) {
        String errorResponseData = e.getResponse().getData()
        assert e.statusCode == statusCode
        assert errorResponseData.contains(/message=${message}/)
        errorResponseData
    }

}
