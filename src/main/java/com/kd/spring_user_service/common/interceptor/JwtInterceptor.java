package com.kd.spring_user_service.common.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kd.spring_user_service.model.Response;
import com.kd.spring_user_service.service.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JWTService jwtService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtInterceptor(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            // Set the response status
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            Response errorResponse = Response.unauthorized("Unauthorized");
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);

            // Write the JSON response body
            response.getWriter().write(jsonResponse);
            return false;
        }

        String token = authorizationHeader.substring(7);
        Integer roleId;
        try {
            roleId = jwtService.extractRoleId(token);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token");
            return false;
        }

        request.setAttribute("token", token);
        request.setAttribute("roleId", roleId);
        return true;
    }
}
