package net.twilightcity.logging;

import java.io.IOException;
import java.util.Collection;

public interface RequestAdapter {

    String getMethod();

    String getRequestURI();

    String getRequestPayload(LoggerSupport loggerSupport) throws IOException;

    Collection<String> getHeaderNames();

    Collection<String> getHeaders(String key);

}
