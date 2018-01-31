package org.dreamscale.logging

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase
import org.slf4j.LoggerFactory

import java.util.concurrent.atomic.AtomicBoolean

class LogbackCaptureAppender extends AppenderBase<ILoggingEvent> {

    private static LogbackCaptureAppender INSTANCE = new LogbackCaptureAppender()

    static LogbackCaptureAppender getInstance() {
        return INSTANCE
    }

    private AtomicBoolean enabled = new AtomicBoolean(false)
    private List<ILoggingEvent> eventList = Collections.synchronizedList([])

    @Override
    protected void append(ILoggingEvent event) {
        if (INSTANCE.enabled.get()) {
            INSTANCE.eventList.add(event)
        }
    }

    @Override
    String toString() {
        return eventList.toString()
    }

    List<ILoggingEvent> getEvents() {
        eventList
    }

    ILoggingEvent getEventWithContent(String content) {
        eventList.find {
            it.message.contains(content)
        }
    }

    void assertEventWithContent(String content) {
        assert getEventWithContent(content) != null
    }

    ILoggingEvent getEventWithRegex(String regex) {
        eventList.find {
            it.message =~ regex
        }
    }

    void assertEventWithRegex(String regex) {
        assert getEventWithRegex(regex) != null
    }

    void enable() {
        eventList.clear()
        enabled.set(true)
    }

    void disable() {
        enabled.set(false)
    }

}
