package net.twilightcity.converters

import spock.lang.Specification

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import static ARandom.aRandom

class LocalDateTimePersistenceConverterSpec extends Specification {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS"
    LocalDateTimePersistenceConverter localDateTimePersistenceConverter = new LocalDateTimePersistenceConverter()

    def "should convert LocalDateTime to java.sql.Timestamp"(){
        given:
        LocalDateTime localDateTime = aRandom.localDateTime()

        when:
        Timestamp sqlTimeStamp = localDateTimePersistenceConverter.convertToDatabaseColumn(localDateTime)

        then:
        def formattedLocalDateTime = localDateTime.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT))
        assert(timeStampString(sqlTimeStamp) == formattedLocalDateTime)
    }

    def "should return null if LocalDateTime is null"(){
        expect:
        assert localDateTimePersistenceConverter.convertToDatabaseColumn(null) == null
    }

    def "should convert to TimeStamp to LocalDateTime"(){
        given:
        Timestamp timestamp = aRandom.timeStamp()

        when:
        LocalDateTime localDateTime = localDateTimePersistenceConverter.convertToEntityAttribute(timestamp)

        then:
        assert localDateTime.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)) == timestamp.format(DATE_TIME_FORMAT)
    }

    def "should return null if TimeStamp is null"(){
        expect:
        assert localDateTimePersistenceConverter.convertToEntityAttribute(null) == null
    }

    private String timeStampString(def timeStamp){
        return new SimpleDateFormat(DATE_TIME_FORMAT).format(timeStamp);
    }
}
