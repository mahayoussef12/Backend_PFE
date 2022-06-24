

package com.isima.projet.Security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration

public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] allowedOrigins = {"*"};
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT")
                .allowedOrigins(allowedOrigins);
    }


}






