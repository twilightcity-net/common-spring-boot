package net.twilightcity.mapper

import org.dreamscale.testsupport.BeanCompare
import spock.lang.Specification

class ApiEntityMapperSpec extends Specification {

    private ApiEntityMapper<Source, Target> typedMapper = new ApiEntityMapper<>(Source, Target)
    private BeanCompare beanCompare = new BeanCompare().excludeFields("sourceVar", "targetVar")

    def "should map api to entity"() {
        given:
        Source api = Source.builder()
                .id("id")
                .build()

        expect:
        beanCompare.assertEquals(api, typedMapper.toEntity(api))

        and:
        beanCompare.assertEquals([api], typedMapper.toEntityList([api]))
    }

    def "should map entity to api"() {
        given:
        Target entity = Target.builder()
                .id("id")
                .build()

        expect:
        beanCompare.assertEquals(entity, typedMapper.toApi(entity))

        and:
        beanCompare.assertEquals([entity], typedMapper.toApiList([entity]))
    }

    def "toEntity should invoke onEntityConversion if input object is not null"() {
        given:
        typedMapper = Spy(ApiEntityMapper, constructorArgs: [Source, Target])

        when:
        typedMapper.toEntity(null)

        then:
        0 * typedMapper.onEntityConversion(_, _)

        when:
        typedMapper.toEntity(new Source(id: "id"))

        then:
        1 * typedMapper.onEntityConversion(_, _) >> { Source api, Target target ->
            beanCompare.assertEquals(api, target)
        }
    }

    def "toApi should invoke onApiConversion if input object is not null"() {
        given:
        typedMapper = Spy(ApiEntityMapper, constructorArgs: [Source, Target])

        when:
        typedMapper.toApi(null)

        then:
        0 * typedMapper.onApiConversion(_, _)

        when:
        typedMapper.toApi(new Target(id: "id"))

        then:
        1 * typedMapper.onApiConversion(_, _) >> { Target entity, Source api ->
            beanCompare.assertEquals(entity, api)
        }
    }

}
