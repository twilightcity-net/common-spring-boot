package org.dreamscale.http;

public interface HttpStatus {

    // --- 2xx Success ---

    /** {@code 204 No Content} (HTTP/1.0 - RFC 1945) */
    int SC_NO_CONTENT = 204;
    /** {@code 205 Reset Content} (HTTP/1.1 - RFC 2616) */
    int SC_RESET_CONTENT = 205;

    // --- 3xx Redirection ---

    /** {@code 303 See Other} (HTTP/1.1 - RFC 2616) */
    int SC_SEE_OTHER = 303;

    // --- 4xx Client Error ---

    /** {@code 400 Bad Request} (HTTP/1.1 - RFC 2616) */
    int SC_BAD_REQUEST = 400;
    /** {@code 401 Unauthorized} (HTTP/1.0 - RFC 1945) */
    int SC_UNAUTHORIZED = 401;
    /** {@code 403 Forbidden} (HTTP/1.0 - RFC 1945) */
    int SC_FORBIDDEN = 403;
    /** {@code 404 Not Found} (HTTP/1.0 - RFC 1945) */
    int SC_NOT_FOUND = 404;
    /** {@code 409 Conflict} (HTTP/1.1 - RFC 2616) */
    int SC_CONFLICT = 409;
    /** {@code 410 Gone} (HTTP/1.1 - RFC 2616) */

    // --- 5xx Server Error ---

    /** {@code 500 Server Error} (HTTP/1.0 - RFC 1945) */
    int SC_INTERNAL_SERVER_ERROR = 500;

}