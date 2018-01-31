package org.dreamscale.springboot.rest

import org.dreamscale.exception.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping(path = "/widgets", produces = MediaType.APPLICATION_JSON_VALUE)
class CrudResource {

    Map<Long, Widget> widgetMap = [:]

    @GetMapping
    List<Widget> findMany() {
        return widgetMap.values().toList()
    }

    @GetMapping("/{id}")
    Widget find(@PathVariable Long id) {
        Widget widget = widgetMap[id]
        if (widget) {
            return widget
        } else {
            throw new NotFoundException("No widget with id=%s", id)
        }
    }

    @PostMapping
    ResponseEntity create(@RequestBody Widget widget) {
        widgetMap[widget.id] = widget
        ResponseEntity.status(HttpStatus.CREATED).body(widget)
    }

    @PutMapping("/{id}")
    ResponseEntity update(@PathVariable Long id, @RequestBody Widget widget) {
        widgetMap[id] = widget
        ResponseEntity.ok().body(widget)
    }

    @DeleteMapping("/{id}")
    ResponseEntity delete(@PathVariable Long id) {
        widgetMap.remove(id)
        ResponseEntity.ok().build()
    }

    @PostMapping(path = "/text", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    String getText(@RequestBody String text) {
        text
    }

    @PostMapping(path = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    String uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        multipartFile.inputStream.text
    }

}
