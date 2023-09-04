package br.tads.ufpr.univangateway;

import br.tads.ufpr.univangateway.config.UriConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.reactive.ServerHttpRequest;

@SpringBootApplication
@EnableConfigurationProperties(UriConfiguration.class)
public class UnivanGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnivanGatewayApplication.class, args);
	}

	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder, UriConfiguration uriConfiguration) {
		final String carpoolService = uriConfiguration.getCarpoolService();
		final String httpBin = uriConfiguration.getHttpBin();

		return builder.routes()
				.route(p -> p.path("/get")
							.filters(f -> f.addRequestHeader("Hello", "World"))
							.uri(httpBin))
				.route(p -> p.path("/carpool/**")
						.filters(f -> f.filter((exchange, chain) -> {
							ServerHttpRequest request = exchange.getRequest();
							String frontendPath = request.getURI().getRawPath();
							String backendPath = frontendPath.replaceAll("/carpool", "/api");

							ServerHttpRequest redirectRequest = request.mutate().path(backendPath).build();
							return chain.filter(exchange.mutate().request(redirectRequest).build());
						}))
						.uri(carpoolService))
				.build();
	}
}
