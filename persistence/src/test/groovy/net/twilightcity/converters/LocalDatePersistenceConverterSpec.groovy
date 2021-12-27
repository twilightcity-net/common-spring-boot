package net.twilightcity.converters

import spock.lang.Specification
import java.time.LocalDate
import static ARandom.aRandom;

class LocalDatePersistenceConverterSpec extends Specification {

    LocalDatePersistenceConverter localDatePersistenceConverter = new LocalDatePersistenceConverter()

    def "should convert LocalDate to a java.sql.Date"(){
        given:
        LocalDate localDate = aRandom.localDate()

        when:
        def sqlDate = localDatePersistenceConverter.convertToDatabaseColumn(localDate)

        then:
        assert (sqlDate.toString() == localDate.toString())
        assert (sqlDate instanceof java.sql.Date)
    }

    def "should return null if LocalDate is null"(){
        when:
        def sqlDate = localDatePersistenceConverter.convertToDatabaseColumn(null)

        then:
        assert sqlDate == null
    }

    def "should convert java.sql.Date to LocalDate"(){
        given:
        java.sql.Date date = aRandom.sqlDate()

        when:
        def localDate = localDatePersistenceConverter.convertToEntityAttribute(date)

        then:
        assert (localDate.toString() == date.toString())
        assert (localDate instanceof LocalDate)
    }

    def "should return null if java.sql.Date is null"(){
        when:
        def localDate = localDatePersistenceConverter.convertToEntityAttribute(null)

        then:
        assert localDate == null
    }
}
