package org.dreamscale.logging

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase

class LogbackCaptureAppender extends AppenderBase<ILoggingEvent> {

    private static LogbackCaptureAppender INSTANCE = new LogbackCaptureAppender();

    static LogbackCaptureAppender getInstance() {
        return INSTANCE;
    }

    private boolean enabled = false
    private List<ILoggingEvent> eventList = []

    @Override
    protected void append(ILoggingEvent event) {
        LogbackCaptureAppender appender = LogbackCaptureAppender.getInstance();
        if (appender.enabled) {
            appender.eventList << event
        }
    }

    @Override
    String toString() {
        return eventList;
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
        enabled = true
    }

    void disable() {
        enabled = false
        eventList.clear()
    }

}
