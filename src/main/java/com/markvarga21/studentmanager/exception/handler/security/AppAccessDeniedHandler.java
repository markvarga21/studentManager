package com.markvarga21.studentmanager.exception.handler.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * A handler for access denied exceptions.
 */
@Component
public class AppAccessDeniedHandler implements AccessDeniedHandler {
    /**
     * This method is used to handle access denied exceptions.
     *
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @param accessDeniedException The access denied exception.
     * @throws IOException If an input or output exception occurs.
     */
    @Override
    public void handle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AccessDeniedException accessDeniedException
    ) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getOutputStream()
                .println(this.getErrorMessage(accessDeniedException.getMessage()));
    }

    private String getErrorMessage(final String exceptionMessage) {
        return String.format("""
        {
            "error": "Access denied",
            "message": "%s"
        }""", exceptionMessage);
    }
}
