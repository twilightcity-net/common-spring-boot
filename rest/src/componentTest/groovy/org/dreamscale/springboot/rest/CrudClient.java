package org.dreamscale.springboot.rest;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.dreamscale.feign.FormUploadClient;

import java.io.File;
import java.util.List;

@Headers({
        "Content-Type: application/json",
        "Accept: application/json",
})
public interface CrudClient extends FormUploadClient {

    @RequestLine("GET /widgets")
    List<Widget> findMany();

    @RequestLine("GET /widgets/{id}")
    Widget find(@Param("id") Long id);

    @RequestLine("POST /widgets")
    Widget create(Widget widget);

    @RequestLine("PUT /widgets/{id}")
    Widget update(@Param("id") Long id, Widget widget);

    @RequestLine("DELETE /widgets/{id}")
    void delete(@Param("id") Long id);

    @Headers({"Content-Type: text/plain", "Accept: text/plain"})
    @RequestLine("POST /widgets/text")
    String getText(String text);

    @Headers({"Content-Type: multipart/form-data", "Accept: text/plain"})
    @RequestLine("POST " + "/widgets/uploadFile")
    String uploadFile(@Param("file") File file);

}
