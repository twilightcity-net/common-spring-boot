package net.twilightcity.mapper

import org.dreamscale.testsupport.BeanCompare
import net.twilightcity.mapper.DozerMapper
import net.twilightcity.mapper.DozerMapperFactory
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime

class DozerMapperSpec extends Specification {

    private DozerMapper entityMapper = new DozerMapperFactory().createDozerMapper()
    private BeanCompare beanCompare = new BeanCompare().excludeFields("sourceVar", "targetVar")

    def "mapIfNotNull should return null on null input"() {
        expect:
        entityMapper.mapIfNotNull(null, Object.class) == null
    }

    def "mapIfNotNull should copy variables"() {
        given:
        Source source = new Source(id: "anId")

        expect:
        beanCompare.assertEquals(source, entityMapper.mapIfNotNull(source, Target))
    }

    def "mapList should map list of objects"() {
        given:
        List sources = [new Source(id: "1"), new Source(id: "2")]

        expect:
        beanCompare.assertEquals(sources, entityMapper.mapList(sources, Target))
    }

    def "should copy types defined in defaultDozerConfig.xml by referenced"() {
        given:
        Source source = Source.builder()
                .id("5")
                .uuid(UUID.randomUUID())
                .localDate(LocalDate.now())
                .localDateTime(LocalDateTime.now())
                .zonedDateTime(ZonedDateTime.now())
                .build()

        when:
        Target target = entityMapper.mapIfNotNull(source, Target)

        then:
        assert source.uuid.is(target.uuid)
        assert source.localDate.is(target.localDate)
        assert source.localDateTime.is(target.localDateTime)
        assert source.zonedDateTime.is(target.zonedDateTime)
        beanCompare.assertEquals(source, target)
    }

}
