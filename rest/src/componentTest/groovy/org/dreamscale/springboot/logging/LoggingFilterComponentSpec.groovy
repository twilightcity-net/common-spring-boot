package org.dreamscale.springboot.logging

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import org.dreamscale.ComponentTest
import org.dreamscale.logging.LogbackCaptureAppender
import org.dreamscale.springboot.crud.CrudClient
import org.dreamscale.springboot.crud.CrudResource
import org.dreamscale.springboot.crud.Widget
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
        logger = LoggerFactory.getLogger("org.dreamscale")
        originalLevel = logger.getLevel()
    }

    def cleanup() {
        logbackCaptureAppender.disable()
        logger.setLevel(originalLevel)
    }

    def "should only log GET payload when TRACE is enabled" () {
        given:
        crudResource.widgetMap[5L] = new Widget(5)

        when:
        logger.setLevel(Level.DEBUG)
        crudClient.find(5)

        then:
        logbackCaptureAppender.assertEventWithContent('Server Request: [GET /widgets/5], Headers:')
        logbackCaptureAppender.assertEventWithRegex('Server Response: \\[GET /widgets/5 < 200\\]$')

        when:
        logger.setLevel(Level.TRACE)
        crudClient.find(5)

        then:
        logbackCaptureAppender.assertEventWithContent('Server Request: [GET /widgets/5], Headers:')
        logbackCaptureAppender.assertEventWithContent('Server Response: [GET /widgets/5 < 200], Payload: [{"id":5}]')
    }

    def "should log POST payload"() {
        when:
        crudClient.create(new Widget(5))

        then:
        logbackCaptureAppender.assertEventWithContent('Server Request: [POST /widgets], Payload: [{"id":5}]')
        logbackCaptureAppender.assertEventWithContent( 'Server Response: [POST /widgets < 201], Payload: [{"id":5}]')
    }

}
