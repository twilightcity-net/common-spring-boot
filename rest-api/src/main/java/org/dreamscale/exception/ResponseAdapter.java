package org.dreamscale.exception;

import java.util.Collection;

public interface ResponseAdapter {

    int getStatusCode();

    Collection<String> getHeaders(String key);

    ErrorEntity getContentAsErrorEntity() throws Exception;

    String getContentAsString() throws Exception;

}
