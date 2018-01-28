package org.dreamscale.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestContext {

    private static ThreadLocal<RequestContext> contexts = new ThreadLocal<>();

    public static RequestContext get() {
        return contexts.get();
    }

    public static void set(RequestContext context) {
        contexts.set(context);
    }

    public static void clear() {
        contexts.remove();
    }


    private String requestId;
    private String sessionId;

}
