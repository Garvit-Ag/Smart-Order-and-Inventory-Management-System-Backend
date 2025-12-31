//package com.oims;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.web.cors.CorsConfiguration;
//
//import java.util.List;
//
//@Configuration
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//
//        return http
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
//                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
//                .authorizeExchange(ex -> ex
//                        // 🔓 Public endpoints (GENERAL PUBLIC)
//                        .pathMatchers("/auth/**").permitAll()
//                        .pathMatchers("/USERSERVICE/**").permitAll()
//                        .pathMatchers(HttpMethod.GET, "/product/**").permitAll()
//
//                        // 👤 Authenticated user endpoints (both CUSTOMER and ADMIN)
//                        .pathMatchers("/auth/profile").authenticated()
//
//                        // 👤 Booking endpoints (accessible to authenticated customers and admins)
//                        .pathMatchers("/order/**").hasAnyRole("CUSTOMER", "ADMIN")
//
//                        // 🛠 Admin-only flight management
//                        .pathMatchers(HttpMethod.POST, "/flights/**").hasRole("ADMIN")
//                        .pathMatchers(HttpMethod.PUT, "/flights/**").hasRole("ADMIN")
//                        .pathMatchers(HttpMethod.DELETE, "/flights/**").hasRole("ADMIN")
//
//                        // Everything else must be authenticated
//                        .anyExchange().authenticated()
//                )
//                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
////                .cors(cors -> cors.configurationSource(exchange -> {
////        CorsConfiguration config = new CorsConfiguration();
////        config.setAllowedOrigins(List.of("http://localhost:4200"));
////        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
////        config.setAllowedHeaders(List.of("*"));
////        config.setAllowCredentials(true);
////                    return config;
////                }))
//                .build();
//    }
//}