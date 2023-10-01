package br.tads.ufpr.univangateway.config;

import br.tads.ufpr.univangateway.filter.AuthenticationFilter;
import br.tads.ufpr.univangateway.service.PathMapping;
import br.tads.ufpr.univangateway.service.RouterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayConfig.class);
    private RouterService service;
    private AuthenticationFilter authenticationFilter;

    @Autowired
    public GatewayConfig(RouterService service, AuthenticationFilter authenticationFilter) {
        this.service = service;
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path(pathRegex(PathMapping.CARPOOL))
                        .filters(f -> f
                                .filter(authenticationFilter)
                                .filter((exchange, chain) -> redirectFilter(exchange, chain, PathMapping.CARPOOL))
                        )
                        .uri(service.getCarpoolUri()))
                .route(p -> p.path(pathRegex(PathMapping.UNIVAN))
                        .filters(f -> f
                                .filter(authenticationFilter)
                                .filter((exchange, chain) -> redirectFilter(exchange, chain, PathMapping.UNIVAN))
                        )
                        .uri(service.getUnivanUri()))
                .route(p -> p.path(pathRegex(PathMapping.LOGIN))
                        .filters(f -> f.filter(((exchange, chain) -> redirectFilter(exchange, chain, PathMapping.LOGIN))))
                        .uri(service.getUnivanUri()))
                .build();
    }

    private static Mono<Void> redirectFilter(ServerWebExchange exchange, GatewayFilterChain chain, PathMapping pathMapping) {
        String rawPath = exchange.getRequest().getURI().getRawPath();
        String backendPath = rawPath.replaceAll(pathMapping.getFrontendPath(), pathMapping.getBackendReplacement());
        LOGGER.info("Redirecionando: [{}] -> [{}]", rawPath, backendPath);

        ServerHttpRequest redirectedRequest = exchange.getRequest().mutate().path(backendPath).build();
        return chain.filter(exchange.mutate().request(redirectedRequest).build());
    }

    private static String pathRegex(PathMapping path) {
        return path.getFrontendPath() + "/**";
    }
}
