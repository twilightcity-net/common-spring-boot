package net.twilightcity.springboot.exception

import net.twilightcity.ComponentTest
import net.twilightcity.exception.BadRequestException
import net.twilightcity.exception.ConflictException
import net.twilightcity.exception.NotFoundException
import net.twilightcity.exception.WebApplicationException
import net.twilightcity.http.HttpStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import spock.lang.Specification

@ComponentTest
class ClientExceptionTranslationSpec extends Specification {

    @Value("\${server.port}")
    String port

    @Autowired
    ExceptionClient exceptionClient

    def "should translate BadRequestException"() {
        when:
        exceptionClient.throwBadRequest()

        then:
        BadRequestException ex = thrown()
        assert ex.statusCode == HttpStatus.SC_BAD_REQUEST
    }

    def "should translate ConflictException"() {
        when:
        exceptionClient.throwConflict()

        then:
        ConflictException ex = thrown()
        assert ex.statusCode == HttpStatus.SC_CONFLICT
    }

    def "should translate NotFoundException"() {
        when:
        exceptionClient.throwNotFound()

        then:
        NotFoundException ex = thrown()
        assert ex.statusCode == HttpStatus.SC_NOT_FOUND
    }

    def "should translate ErrorEntity and retain message"() {
        when:
        exceptionClient.throwNotFoundWithMessage()

        then:
        NotFoundException ex = thrown()
        assert ex.statusCode == HttpStatus.SC_NOT_FOUND
        assert ex.message == "was not found"
    }

    def "should translate UNSUPPORTED_MEDIA_TYPE to WebApplicationException"() {
        when:
        exceptionClient.inputMediaTypeMismatch("input")

        then:
        WebApplicationException ex = thrown()
        assert ex.statusCode == HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE
        assert ex.errorEntity.message == "Content type 'application/json' not supported"
    }

    def "should translate NOT_ACCEPTABLE to WebApplicationException"() {
        when:
        exceptionClient.outputMediaTypeMismatch("input")

        then:
        WebApplicationException ex = thrown()
        assert ex.statusCode == HttpStatus.SC_NOT_ACCEPTABLE
        assert ex.errorEntity.message == "Could not find acceptable representation"
    }

    def "should return ErrorEntity with message and status if caller accepts text/plain but not application/json"() {
        when:
        exceptionClient.returnErrorEntityWithStatusAndMessageIfAcceptTextPlainAndNotJson("input")

        then:
        WebApplicationException ex = thrown()
        assert ex.statusCode == HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE
        assert ex.message == "Content type 'application/json' not supported"
    }

    def "should return ErrorEntity with status caller does not accept text/plain or application/json"() {
        when:
        exceptionClient.returnErrorEntityWithStatusIfAcceptTypeNotTextPlainOrJson("input")

        then:
        WebApplicationException ex = thrown()
        assert ex.statusCode == HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE
        assert ex.message == null
    }

}
