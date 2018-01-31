package org.dreamscale.springboot.exception;

import feign.Headers;
import feign.RequestLine;
import org.dreamscale.springboot.rest.Widget;

@Headers({
        "Content-Type: application/json",
        "Accept: application/json",
})
public interface ExceptionClient {

    @RequestLine("POST /exception/validateInputEntity")
    void validateInputEntity(ValidatableInput input);

    default void throwBadRequest() {
        validateInputEntity(new ValidatableInput());
    }

    @RequestLine("POST /exception/notAutoConstructableEntity")
    void validateInputEntity(NotAutoConstructableInput input);

    @RequestLine("GET /exception/invalidPath")
    void throwNotFound();

    @RequestLine("GET /exception/forbidden")
    void throwForbidden();

    @RequestLine("POST /exception/conflict")
    void throwConflict();

    @Headers({"Accept: application/json,text/xml", "Content-Type: application/json"})
    @RequestLine("POST /exception/mediaTypeMismatch")
    String inputMediaTypeMismatch(String input);

    @Headers({"Accept: application/json", "Content-Type: text/plain"})
    @RequestLine("POST /exception/mediaTypeMismatch")
    String outputMediaTypeMismatch(String input);

    @Headers({"Accept: text/plain", "Content-Type: application/json"})
    @RequestLine("POST /exception/mediaTypeMismatch")
    String returnErrorEntityWithStatusAndMessageIfAcceptTextPlainAndNotJson(String input);

    @Headers({"Accept: text/xml", "Content-Type: application/json"})
    @RequestLine("POST /exception/mediaTypeMismatch")
    String returnErrorEntityWithStatusIfAcceptTypeNotTextPlainOrJson(String input);

    @RequestLine("GET /exception/notFoundExceptionWithMessage")
    Widget throwNotFoundWithMessage();

}
