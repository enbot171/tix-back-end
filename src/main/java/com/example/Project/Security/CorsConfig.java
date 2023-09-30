package com.example.Project.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Allow requests from this origin (e.g., http://localhost:3000)
        config.addAllowedOrigin("http://localhost:3000");

        config.addAllowedMethod("*");

        config.setAllowedMethods(Arrays.asList("POST", "GET", "OPTIONS", "DELETE"));

        config.addAllowedHeader("*");

        config.addExposedHeader("Set-Cookie");

        config.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}






