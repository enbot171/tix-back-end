// package com.example.Project.Security;

// //Security Configuration to handle Tokens & Passwords

// import com.example.Project.Security.AuthEntryPointJwt;
// import com.example.Project.Security.AuthTokenFilter;
// import com.example.Project.User.UserDetailsServiceImpl;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;

// import java.util.Arrays;


// @Configuration
// @EnableWebSecurity
// @EnableMethodSecurity
// public class WebSecurityConfig{
//     @Autowired
//     UserDetailsServiceImpl userDetailsService;

//     @Autowired
//     private AuthEntryPointJwt unauthorizedHandler;

//     @Bean
//     public AuthTokenFilter authenticationJwtTokenFilter() {
//         return new AuthTokenFilter();
//     }   

//     // @Bean
//     // CorsConfigurationSource corsConfigurationSource() {
//     //     CorsConfiguration configuration = new CorsConfiguration();
//     //     configuration.setAllowedOrigins(Arrays.asList("localhost:3000","https://cs203front.azurewebsites.net"));
//     //     configuration.setAllowedMethods(Arrays.asList("*"));
//     //     configuration.setAllowedHeaders(Arrays.asList("*"));
//     //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//     //     source.registerCorsConfiguration("/**", configuration);
//     //     return source;
//     // }

//     @Bean
//     public DaoAuthenticationProvider authenticationProvider() {
//         DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//         authProvider.setUserDetailsService(userDetailsService);
//         authProvider.setPasswordEncoder(passwordEncoder());
//         return authProvider;
//     }

//     @Bean
//     public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//         return authConfig.getAuthenticationManager();
//     }

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         http.csrf(csrf -> csrf.disable())
//                 .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
//                 .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                 .authorizeHttpRequests(auth ->
//                         auth.requestMatchers("/api/v1/auth/**").permitAll()
//                                 .requestMatchers("/api/v1/events/getEventByNameDate/{eventName}/{eventDate}/ticketByCategory/{category}/allSeats/{seatNum}/purchase/{userId}").permitAll()
//                                 .requestMatchers("/api/v1/purchases/**").permitAll()
//                                 .anyRequest().permitAll()
//                 );

//         http.authenticationProvider(authenticationProvider());
//         http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//         // http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

//         return http.build();
//     }
// }

package com.example.Project.Security;

//Security Configuration to handle Tokens & Passwords

import com.example.Project.User.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig{
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(withDefaults()).csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/v1/auth/**").permitAll()
                                .requestMatchers("/api/v1/buy/{userId}/{eventName}").permitAll()
                                .requestMatchers("api/v1/queueNum/{eventName}/{userId}").permitAll()
                                .anyRequest().permitAll()
                );


        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Allow requests from this origin (e.g., http://localhost:3000)
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000","https://cs203front.azurewebsites.net/"));

        config.setAllowedMethods(Arrays.asList("*"));

        config.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS"));

        config.setAllowedHeaders(Arrays.asList("*"));

        config.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
