package com.markvarga21.studentmanager.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * The entry point for the authentication regarding the authentication.
 */
@Component
public class AppAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * This method is used to commence the authentication.
     *
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @param authException The authentication exception.
     * @throws IOException If an input or output exception occurs.
     */
    @Override
    public void commence(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AuthenticationException authException
    ) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream()
                .println(this.getErrorMessage(authException.getMessage()));
    }

    private String getErrorMessage(final String exceptionMessage) {
        return String.format("""
        {
            "error": "Unauthorized",
            "message": "%s"
        }""", exceptionMessage);
    }
}
