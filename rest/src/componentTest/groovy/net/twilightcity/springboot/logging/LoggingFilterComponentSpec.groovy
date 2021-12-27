package net.twilightcity.springboot.logging

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import net.twilightcity.ComponentTest
import net.twilightcity.exception.NotFoundException
import net.twilightcity.logging.LogbackCaptureAppender
import net.twilightcity.springboot.rest.CrudClient
import net.twilightcity.springboot.rest.CrudResource
import net.twilightcity.springboot.rest.Widget
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

@ComponentTest
class LoggingFilterComponentSpec extends Specification {

    @Autowired
    CrudClient crudClient
    @Autowired
    CrudResource crudResource
    LogbackCaptureAppender logbackCaptureAppender = LogbackCaptureAppender.getInstance()
    Logger logger
    Level originalLevel

    def setup() {
        logbackCaptureAppender.enable()
        logger = LoggerFactory.getLogger("net.twilightcity")
        originalLevel = logger.getLevel()
    }

    def cleanup() {
        logbackCaptureAppender.disable()
        logger.setLevel(originalLevel)
    }

    def "should only log GET payload when TRACE is enabled"() {
        given:
        crudResource.widgetMap[5L] = new Widget(5)

        when:
        logger.setLevel(Level.DEBUG)
        crudClient.find(5)

        then:
        logbackCaptureAppender.assertEventWithContent('Server Request: [GET /widgets/5], Headers:')
        logbackCaptureAppender.assertEventWithRegex('Server Response: \\[GET /widgets/5 < 200\\], ElapsedTime: \\d+ms$')

        when:
        logger.setLevel(Level.TRACE)
        crudClient.find(5)

        then:
        logbackCaptureAppender.assertEventWithContent('Server Request: [GET /widgets/5], Headers:')
        logbackCaptureAppender.assertEventWithRegex(/Server Response: \[GET \/widgets\/5 < 200\], ElapsedTime: \d+ms, Payload: \[\{"id":5\}\]/)
    }

    def "should log POST payload"() {
        when:
        crudClient.create(new Widget(5))

        then:
        logbackCaptureAppender.assertEventWithContent('Server Request: [POST /widgets], Payload: [{"id":5}]')
        logbackCaptureAppender.assertEventWithRegex(/Server Response: \[POST \/widgets < 201\], ElapsedTime: \d+ms, Payload: \[\{"id":5\}\]/)
    }

    def "should log ErrorEntity when exception is thrown"() {
        when:
        crudClient.find(1)

        then:
        thrown(NotFoundException)

        and:
        logbackCaptureAppender.assertEventWithRegex(/Server Response: \[GET \/widgets\/1 < 404\], ElapsedTime: \d+ms, Payload: \[\{"message":"No widget with id=1","args":\[1\]\}\]/)
    }

}
