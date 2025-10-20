package com.blitzdev.gateway_server.filter;

import com.blitzdev.gateway_server.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
public class FilterUtils {

    private static JwtService jwtService;
    private static final Set<String> WHITELISTED_PATHS = Set.of("/lms/api/v1/auth/**");

    public FilterUtils(JwtService jwtService) {
        FilterUtils.jwtService = jwtService;
    }

    public static ServerWebExchange addUserIdToRequest(String authHeader, ServerWebExchange exchange) {

        String jwtToken = authHeader.substring(7);
        String userId = jwtService.extractUserId(jwtToken);

        // add user id to request.
        ServerHttpRequest updatedRequest = exchange
                .getRequest()
                .mutate()
                .header("user-id", userId)
                .build();

        return exchange.mutate().request(updatedRequest).build();
    }

    public static Mono<Void> rejectRequest(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    public static boolean isWhiteListedPath(String path) {
        return WHITELISTED_PATHS.stream().anyMatch(path::startsWith);
    }

}
