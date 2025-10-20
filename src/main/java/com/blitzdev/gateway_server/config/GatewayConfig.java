package com.blitzdev.gateway_server.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/lms/api/v1/auth/**").uri("lb://lms-auth-service"))
                .route(p -> p.path("/lms/api/v1/lead/**").uri("lb://lms-lead-service"))
                .build();
    }
}