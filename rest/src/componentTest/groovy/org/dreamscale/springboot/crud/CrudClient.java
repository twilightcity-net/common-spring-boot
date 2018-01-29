package org.dreamscale.springboot.crud;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

@Headers({
        "Content-Type: application/json",
        "Accept: application/json",
})
public interface CrudClient {

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

}
