package net.twilightcity.springboot.rest;

import net.twilightcity.exception.NotFoundException;
import org.apache.commons.io.IOUtils;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codehaus.groovy.runtime.IOGroovyMethods;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/widgets", produces = MediaType.APPLICATION_JSON_VALUE)
public class CrudResource {
    @GetMapping
    public List<Widget> findMany() {
        return DefaultGroovyMethods.toList(widgetMap.values());
    }

    @GetMapping("/{id}")
    public Widget find(@PathVariable Long id) {
        Widget widget = widgetMap.get(id);
        if (DefaultGroovyMethods.asBoolean(widget)) {
            return widget;
        } else {
            throw new NotFoundException("No widget with id=%s", id);
        }

    }

    @PostMapping
    public ResponseEntity create(@RequestBody Widget widget) {
        widgetMap.put(widget.getId(), widget);
        return ResponseEntity.status(HttpStatus.CREATED).body(widget);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody Widget widget) {
        widgetMap.put(id, widget);
        return ResponseEntity.ok().body(widget);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        widgetMap.remove(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/text", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String getText(@RequestBody String text) {
        return text;
    }

    @PostMapping(path = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        return IOUtils.toString(multipartFile.getInputStream(), StandardCharsets.UTF_8);
    }

    public Map<Long, Widget> getWidgetMap() {
        return widgetMap;
    }

    public void setWidgetMap(Map<Long, Widget> widgetMap) {
        this.widgetMap = widgetMap;
    }

    private Map<Long, Widget> widgetMap = new LinkedHashMap<Long, Widget>();
}
