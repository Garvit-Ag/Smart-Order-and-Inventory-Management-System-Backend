package com.oims;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()          
                // Public routes - no token required
                .route("public-routes", r -> r
                        .path("/auth/register", "/auth/login", "/auth/test")
                        .uri("lb://UserService"))
                
                // User routes
                .route("admin-routes", r -> r
                        .path("/auth/delete/{id}")
                        .filters(f -> f.filter(new RoleBasedAuthorizationFilter(jwtUtil, List.of("CUSTOMER"))))
                        .uri("lb://UserService"))
                
                // Private routes - token required - Means not role specific
                .route("private-routes", r -> r
                        .path("/auth/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("lb://UserService"))
                
             // ProductService routes
                .route("product-public", r -> r
                		.path("/product/get")
                		.uri("lb://ProductService"))
                .route("product-private", r -> r
                		.path("/products/**")
                		.filters(f -> f.filter(jwtAuthenticationFilter))
                		.uri("lb://ProductService"))
                .build();
    }
}