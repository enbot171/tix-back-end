// package com.example.Project.Security;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
// import org.springframework.web.filter.CorsFilter;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.EnableWebMvc;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// import jakarta.annotation.PostConstruct;

// import java.util.Arrays;

// @Configuration
// @EnableWebMvc
// public class CorsConfig implements WebMvcConfigurer{
//     @Override
//   public void addCorsMappings(CorsRegistry registry) {

//     registry.addMapping("/**")
//       .allowedOrigins("localhost:3000","https://cs203front.azurewebsites.net")
//       .allowedMethods("GET", "POST", "PUT", "DELETE")
//       .allowedHeaders("*")
//       .exposedHeaders("*")
//       .allowCredentials(true).maxAge(3600);

//     // Add more mappings...
//   }
    
//     // @Bean
//     // public CorsFilter corsFilter() {
        
//     //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//     //     CorsConfiguration config = new CorsConfiguration();

//     //     // Allow requests from this origin (e.g., http://localhost:3000)
//     //     config.addAllowedOrigin("*");

//     //     config.addAllowedMethod("*");

//     //     config.setAllowedMethods(Arrays.asList("POST", "GET", "OPTIONS", "DELETE", "PUT"));

//     //     config.addAllowedHeader("*");

//     //     config.addExposedHeader("Set-Cookie");

//     //     config.setAllowCredentials(true);

//     //     source.registerCorsConfiguration("/**", config);
//     //     return new CorsFilter(source);
//     // }
// }






