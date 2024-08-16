package net.twilightcity.converters

import spock.lang.Specification
import java.sql.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import static ARandom.aRandom;

class ZoneDateTimePersistenceConverterSpec extends Specification {

    ZonedDateTimePersistenceConverter zonedDateTimePersistenceConverter = new ZonedDateTimePersistenceConverter()
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS"

    def "should convert ZonedDateTime into TimeStamp"(){
        given:
        ZonedDateTime zonedDateTime = aRandom.zonedDateTime()

        when:
        def timeStamp = zonedDateTimePersistenceConverter.convertToDatabaseColumn(zonedDateTime)

        then:
        def formattedZonedDateTime = zonedDateTime.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT))
        assert (timeStampString(timeStamp) == formattedZonedDateTime)
        assert (timeStamp instanceof Timestamp)
    }

    def "should return null if ZonedDateTime is null"(){
        when:
        def timeStamp = zonedDateTimePersistenceConverter.convertToDatabaseColumn(null)

        then:
        assert timeStamp == null
    }

    def "should convert TimeStamp into ZonedDateTime"(){
        given:
        Timestamp timestamp = Timestamp.valueOf("2007-09-23 10:10:10.0")

        when:
        def zonedDateTime = zonedDateTimePersistenceConverter.convertToEntityAttribute(timestamp)

        then:
        assert timestamp.toInstant() == zonedDateTime.toInstant()
        assert (zonedDateTime instanceof ZonedDateTime)
    }

    def "should return null if TimeStamp is null"(){
        when:
        def zonedDateTime = zonedDateTimePersistenceConverter.convertToEntityAttribute(null)

        then:
        assert zonedDateTime == null
    }

    private String timeStampString(Timestamp timeStamp, String format = DATE_TIME_FORMAT, TimeZone timeZone = null){
        def simpleDateFormat = new SimpleDateFormat(format)
        if (timeZone != null) {
            simpleDateFormat.setTimeZone(timeZone)
        }
        return simpleDateFormat.format(timeStamp);
    }
}
