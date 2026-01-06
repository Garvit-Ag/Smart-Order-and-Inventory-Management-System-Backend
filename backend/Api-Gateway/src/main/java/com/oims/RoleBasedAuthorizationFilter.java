package com.oims;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class RoleBasedAuthorizationFilter implements GatewayFilter {
    private JwtUtil jwtUtil;
    
    private final List<String> allowedRoles;

    public RoleBasedAuthorizationFilter(JwtUtil jwtUtil, List<String> allowedRoles) {
        this.allowedRoles = allowedRoles;
        this.jwtUtil=jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        
        if (!jwtUtil.isTokenValid(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String userRole = jwtUtil.extractRole(token);
        
        if (!allowedRoles.contains(userRole)) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }
        
        // Add user info to headers
        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                .header("X-User-Role", jwtUtil.extractRole(token))
                .header("X-User-Id", jwtUtil.extractUserId(token))
                .header("X-User-Email", jwtUtil.extractEmail(token))
                .build(); // ✅ Saved to variable

        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }
}