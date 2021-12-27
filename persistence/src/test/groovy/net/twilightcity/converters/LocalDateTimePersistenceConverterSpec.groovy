package net.twilightcity.converters

import spock.lang.Specification

import java.text.SimpleDateFormat
import java.time.LocalDateTime;
import java.sql.Timestamp
import java.time.format.DateTimeFormatter;

import static ARandom.aRandom;


class LocalDateTimePersistenceConverterSpec extends Specification {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS"
    LocalDateTimePersistenceConverter localDateTimePersistenceConverter = new LocalDateTimePersistenceConverter()

    def "should convert LocalDateTime to java.sql.Timestamp"(){
        given:
        LocalDateTime localDateTime = aRandom.localDateTime()

        when:
        def sqlTimeStamp = localDateTimePersistenceConverter.convertToDatabaseColumn(localDateTime)

        then:
        def formattedLocalDateTime = localDateTime.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT))
        assert(timeStampString(sqlTimeStamp) == formattedLocalDateTime)
        assert (sqlTimeStamp instanceof Timestamp)
    }

    def "should return null if LocalDateTime is null"(){
        when:
        def sqlTimeStamp = localDateTimePersistenceConverter.convertToDatabaseColumn(null)

        then:
        assert sqlTimeStamp == null
    }

    def "should convert to TimeStamp to LocalDateTime"(){
        given:
        Timestamp timestamp = aRandom.timeStamp()

        when:
        def localDateTime = localDateTimePersistenceConverter.convertToEntityAttribute(timestamp)

        then:
        def formattedTimeStampDate = timestamp.format("yyyy-MM-dd")
        def formattedTimeStampTime = timestamp.format("HH:mm:ss.SSS")
        assert (localDateTime.date.toString() == formattedTimeStampDate)
        assert (localDateTime.time.toString() == formattedTimeStampTime)
        assert (localDateTime instanceof LocalDateTime)
    }

    def "should return null if TimeStamp is null"(){
        when:
        def localDateTime = localDateTimePersistenceConverter.convertToEntityAttribute(null)

        then:
        assert localDateTime == null
    }

    private String timeStampString(def timeStamp){
        return new SimpleDateFormat(DATE_TIME_FORMAT).format(timeStamp);
    }
}
