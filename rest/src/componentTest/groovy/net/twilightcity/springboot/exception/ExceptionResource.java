package net.twilightcity.springboot.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import net.twilightcity.exception.BadRequestException;
import net.twilightcity.exception.ConflictException;
import net.twilightcity.exception.NotFoundException;
import net.twilightcity.springboot.rest.Widget;
import net.twilightcity.springboot.validation.ValidationErrorCodes;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.ValidationException;

@RestController
@RequestMapping(path = "/exception", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExceptionResource {

    @GetMapping("/jsonParseException")
    Widget throwJsonParseException() throws JsonParseException {
        throw new JsonParseException("json parse exception", null);
    }

    @GetMapping("/jsonMappingException")
    Widget throwJsonMappingException() throws JsonMappingException {
        throw new JsonMappingException("json mapping exception");
    }

    @GetMapping("/validationException")
    Widget throwValidationException() {
        throw new ValidationException("mappers exception");
    }

    @GetMapping("/badRequestException")
    Widget throwBadRequestException() {
        throw new BadRequestException(new TestErrorCode(), "Test Error");
    }

    @PostMapping("/validateInputEntity")
    void validateInputEntity(@RequestBody @Valid ValidatableInput input) {
    }

    @PostMapping("/notAutoConstructableEntity")
    void validateInputEntity(@RequestBody NotAutoConstructableInput input) {
    }

    @GetMapping("/httpMediaTypeNotAcceptableException")
    Widget throwHttpMediaTypeNotAcceptableException() throws HttpMediaTypeNotAcceptableException {
        throw new HttpMediaTypeNotAcceptableException("try again");
    }

    @GetMapping("/forbidden")
    void forbidden() {
        throw new IllegalStateException();
    }

    @PostMapping("/conflict")
    Widget conflict() {
        throw new ConflictException(ValidationErrorCodes.ERROR_ENTITY_DATA_FORMAT, "conflict");
    }

    @PostMapping(value = "/mediaTypeMismatch", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    String mediaTypeMismatch(String text) {
        return text;
    }

    @GetMapping("/notFoundExceptionWithMessage")
    Widget throwNotFoundWithMessage() {
        throw new NotFoundException("was not found");
    }

}
