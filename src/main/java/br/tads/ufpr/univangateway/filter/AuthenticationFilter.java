package br.tads.ufpr.univangateway.filter;

import br.tads.ufpr.univangateway.service.JwtService;
import br.tads.ufpr.univangateway.service.RouterService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RouterService routerService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (routerService.isRouteSecured.test(request)) {
            if (this.isAuthorizationHeaderMissing(request)) {
                return this.onError(exchange, HttpStatus.UNAUTHORIZED, "Authorization header is missing in request");
            }

            final String token = this.getAuthHeader(request);

            if (jwtService.isInvalid(token)) {
                return this.onError(exchange, HttpStatus.UNAUTHORIZED, "Authorization header is invalid");
            }

            this.populateRequestWithHeaders(exchange, token);
        }

        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus, String message) {
        LOGGER.error(message);

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request
                .getHeaders()
                .getOrEmpty("Authorization")
                .get(0)
                .substring(7); // "Bearer ".length = 7
    }

    private boolean isAuthorizationHeaderMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, String token) {
        Claims claims = jwtService.getAllClaimsFromToken(token);

        exchange.getRequest().mutate()
                .header("id", String.valueOf(claims.get("id")))
                .header("role", String.valueOf(claims.get("role")))
                .build();
    }
}
