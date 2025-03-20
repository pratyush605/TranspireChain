package org.blockchain.TranspireChain.Security.Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRoleFilter extends OncePerRequestFilter {

    private final StringRedisTemplate redisTemplate;

    public JwtRoleFilter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String authenticationHeader = request.getHeader("Authorization");
            if (authenticationHeader != null && authenticationHeader.startsWith("Bearer ")) {
                String token = authenticationHeader.substring(7);
                String role = redisTemplate.opsForValue().get(token);
                if (role == null) {
                    response.setStatus(401);
                    response.getWriter().write("Unauthorized: Role not found or time limit exceeded.");
                    return;
                }
                request.setAttribute("userRole", role);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e){
            response.getWriter().write(e.getMessage());
        }
    }
}