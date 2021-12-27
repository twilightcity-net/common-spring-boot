package net.twilightcity.springboot.rest


import net.twilightcity.ComponentTest
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

import java.time.Duration

@ComponentTest
class RestRequestComponentSpec extends Specification {

    @Autowired
    CrudResource resource
    @Autowired
    CrudClient crudClient

    def cleanup() {
        resource.widgetMap.clear()
    }

    def 'find'() {
        given:
        resource.widgetMap[1l] = new Widget(1)

        when:
        Widget widget = crudClient.find(1)

        then:
        widget == new Widget(1)
    }

    def 'findMany'() {
        given:
        resource.widgetMap[1l] = new Widget(1)
        resource.widgetMap[2l] = new Widget(2)

        when:
        List<Widget> widgets = crudClient.findMany()

        then:
        widgets == [new Widget(1), new Widget(2)]
    }

    def 'createWithPost'() {
        when:
        crudClient.create(new Widget(1))

        then:
        resource.widgetMap[1l] == new Widget(1)
        resource.widgetMap.size() == 1
    }

    def 'updateWithPut'() {
        given:
        resource.widgetMap[1l] = new Widget(5)

        when:
        crudClient.update(1, new Widget(1))

        then:
        resource.widgetMap[1l] == new Widget(1)
    }

    def 'delete'() {
        given:
        Widget widget = new Widget(1)
        resource.widgetMap[1l] = widget

        when:
        crudClient.delete(widget.id)

        then:
        resource.widgetMap.isEmpty()
    }

    def 'should propagate exception'() {
        when:
        crudClient.find(1)

        then:
        def ex = thrown(net.twilightcity.exception.NotFoundException)
        ex
    }

    def "should encode and decode text"() {
        given:
        String text = "some-text"

        when:
        String response = crudClient.getText(text)

        then:
        assert response == text
    }

    def "should handle file upload"() {
        given:
        String content = "here is some content"
        File fileToUpload = File.createTempFile("fileupload", "csv")
        fileToUpload << content

        expect:
        assert content == crudClient.uploadFile(fileToUpload)
    }

    def "should handle jsr310 serialization support"() {
        given:
        Widget widget = new Widget(id: 1)
        widget.id = 1
        widget.duration = Duration.parse("PT20M")
        resource.widgetMap[widget.id] = widget

        when:
        Widget response = crudClient.find(1)

        then:
        assert response == widget
    }

}
