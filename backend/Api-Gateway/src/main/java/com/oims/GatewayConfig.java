package com.oims;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    private final JwtUtil jwtUtil;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()          
                // Public routes - no token required
                .route("public-routes", r -> r
                        .path("/auth/register", "/auth/login")
                        .uri("lb://UserService"))
                // User routes
                .route("customer-routes", r -> r
                        .path("/auth/delete/{id}")
                        .filters(f -> f.filter(new RoleBasedAuthorizationFilter(jwtUtil, List.of("CUSTOMER"))))
                        .uri("lb://UserService"))
                // Private routes - token required - Means not role specific
                .route("private-routes", r -> r
                        .path("/auth/profile")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("lb://UserService"))
                .route("admin-routes", r -> r
                        .path("/auth/**")
                        .filters(f -> f.filter(new RoleBasedAuthorizationFilter(jwtUtil, List.of("ADMIN"))))
                        .uri("lb://UserService"))
                
             // ProductService routes
                .route("product-public", r -> r
                		.path("/product/get/**")
                		.uri("lb://ProductService"))
                .route("product-private", r -> r
                		.path("/product/**")
                		.filters(f -> f.filter(new RoleBasedAuthorizationFilter(jwtUtil, List.of("ADMIN","WAREHOUSE_MANAGER"))))
                		.uri("lb://ProductService"))
                
                //Order Service
                .route("customer-routes", r -> r
                		.path("/order")
                		.filters(f -> f.filter(new RoleBasedAuthorizationFilter(jwtUtil, List.of("CUSTOMER"))))
                		.uri("lb://OrderService"))
                .route("admin-routes", r -> r
                		.path("/order/**")
                		.filters(f -> f.filter(new RoleBasedAuthorizationFilter(jwtUtil, List.of("ADMIN","SALES_EXECUTIVE"))))
                		.uri("lb://OrderService"))
                
                //Bill Service
                .route("private-routes", r -> r
                		.path("/bill/**")
                		.filters(f -> f.filter(new RoleBasedAuthorizationFilter(jwtUtil, List.of("ADMIN","FINANCE_OFFICER"))))
                		.uri("lb://BillingService"))
                .build();
    }
}