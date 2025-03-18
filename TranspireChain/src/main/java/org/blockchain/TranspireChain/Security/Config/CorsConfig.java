package org.blockchain.TranspireChain.Security.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
//                .allowedHeaders("*")
                .allowedHeaders("Authorization", "Content-Type")
//                .allowedOriginPatterns("*")
                .allowedOriginPatterns("http://localhost:8080")
                .allowedMethods("GET","POST", "PUT", "PATCH", "DELETE", "OPTIONS");
    }
}
