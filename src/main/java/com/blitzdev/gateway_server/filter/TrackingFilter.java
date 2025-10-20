package com.blitzdev.gateway_server.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.blitzdev.gateway_server.filter.FilterUtils.*;

public class TrackingFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(TrackingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        String authHeader = request.getHeaders().getFirst("Authorization");

        // if path is whitelisted, skip auth check to resource.
        if (isWhiteListedPath(request.getURI().getPath())) {
            return chain.filter(exchange);
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return chain.filter(addUserIdToRequest(authHeader,exchange));
        } else { // return unauthorized response
            logger.warn("Unauthorized request to {} {}", request.getMethod(), request.getURI());
            return rejectRequest(exchange);
        }
    }
}