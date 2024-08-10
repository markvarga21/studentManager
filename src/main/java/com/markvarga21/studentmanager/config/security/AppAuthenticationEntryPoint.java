package com.markvarga21.studentmanager.config.security;

import com.google.gson.Gson;
import com.markvarga21.studentmanager.exception.util.AuthError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * The entry point for the application regarding the authentication.
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
        Gson gson = new Gson();
        AuthError authError = AuthError.builder()
                .error("Unauthorized")
                .message(exceptionMessage)
                .status(HttpServletResponse.SC_UNAUTHORIZED)
                .build();
        return gson.toJson(authError);
    }
}
