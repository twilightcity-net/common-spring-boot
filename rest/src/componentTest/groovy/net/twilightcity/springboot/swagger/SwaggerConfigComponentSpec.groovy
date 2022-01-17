package net.twilightcity.springboot.swagger

import net.twilightcity.ComponentTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import spock.lang.Ignore
import spock.lang.Specification

@ComponentTest
class SwaggerConfigComponentSpec extends Specification {

    @Value('${server.port}')
    String port

    @Autowired
    SwaggerClient swaggerClient

    def "swagger docs should be generated at runtime for resources"() {
        when:
        Map swaggerJson = swaggerClient.getSwaggerJson()

        then:
        Map resourcePathsMap = swaggerJson["paths"]
        resourcePathsMap.get("/widgets") != null
    }

}
