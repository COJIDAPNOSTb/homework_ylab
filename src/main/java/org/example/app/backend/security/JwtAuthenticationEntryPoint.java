package org.example.app.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {


        boolean isApiRequest = request.getRequestURI().startsWith("/api") ||
                request.getRequestURI().startsWith("/rest") ||
                request.getHeader("Accept").contains("application/json");

        if (isApiRequest) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("timestamp", new Date());
            errorDetails.put("status", HttpServletResponse.SC_UNAUTHORIZED);
            errorDetails.put("error", "Unauthorized");
            errorDetails.put("message", authException.getMessage());
            errorDetails.put("path", request.getRequestURI());

            response.getWriter().write(new ObjectMapper().writeValueAsString(errorDetails));
        } else {

            response.sendRedirect("/login");
        }
    }
}