package org.blockchain.TranspireChain.Security.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.blockchain.TranspireChain.Security.Model.MyUserDetails;
import org.blockchain.TranspireChain.Security.Service.JwtService;
import org.blockchain.TranspireChain.Security.Service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    public JwtService jwtService;
    @Autowired
    ApplicationContext context;
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try{
            final String authenticationHeader = request.getHeader("Authorization");
            String token = null;
            String email = null;

            if(authenticationHeader != null && authenticationHeader.startsWith("Bearer ")){
                token = authenticationHeader.substring(7);
                email = jwtService.extractUsername(token);
                if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    System.out.println(email);
                    MyUserDetails myUserDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(email);
                    boolean validateToken = this.jwtService.validateToken(token, myUserDetails);
                    if(validateToken){
                        System.out.println("token is valid");
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(myUserDetails, null, myUserDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                    else {
                        System.out.println("token is invalid");
                    }
                }
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e){
            sendErrorResponse(response, 401, "tokenExpired");
        } catch (MalformedJwtException | SignatureException e){
            sendErrorResponse(response, 401, "invalidToken");
        } catch (Exception e){
            sendErrorResponse(response, 500, e.getMessage());
        }
    }
    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(status, message, Instant.now().toEpochMilli());
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }
}
