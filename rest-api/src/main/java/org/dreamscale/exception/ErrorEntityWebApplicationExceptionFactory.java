package org.dreamscale.exception;


import org.dreamscale.http.HttpStatus;

import java.util.Collection;

public class ErrorEntityWebApplicationExceptionFactory {

    public RuntimeException createException(ResponseAdapter response) {
        ErrorEntity errorEntity = createErrorEntity(response);
        return createException(errorEntity, response);
    }

    private ErrorEntity createErrorEntity(ResponseAdapter response) {
        ErrorEntity errorEntity = null;
        String message = null;

        try {
            errorEntity = response.getContentAsErrorEntity();
        } catch (Exception ex) {
            try {
                message = response.getContentAsString();
            } catch (Exception ex2) {
                message = "Failed to read Response";
            }
        }

        if (errorEntity == null) {
            errorEntity = ErrorEntity.builder()
                    .message(message)
                    .build();
        }
        return errorEntity;
    }

    private WebApplicationException createException(ErrorEntity errorEntity, ResponseAdapter response) {
        switch (response.getStatusCode()) {
            case HttpStatus.SC_BAD_REQUEST:
                return new BadRequestException(errorEntity);
            case HttpStatus.SC_FORBIDDEN:
                return new ForbiddenException(errorEntity);
            case HttpStatus.SC_CONFLICT:
                return new ConflictException(errorEntity);
            case HttpStatus.SC_NOT_FOUND:
                return new NotFoundException(errorEntity);
            case HttpStatus.SC_SEE_OTHER:
                Collection<String> locations = response.getHeaders("Location");
                String otherLocation = locations == null || locations.isEmpty() ? "" : locations.iterator().next();
                return new SeeOtherException(otherLocation);
            default:
                return new WebApplicationException(response.getStatusCode(), errorEntity);
        }
    }

}
