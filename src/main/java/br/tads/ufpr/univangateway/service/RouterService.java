package br.tads.ufpr.univangateway.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class RouterService {
    @Value("${microservices.carpool.url}")
    private String carpoolUri;
    @Value("${microservices.univan.url}")
    private String univanUri;
    @Value("${microservices.routes.url}")
    private String routesUri;
    @Value("${microservices.history.url}")
    private String historyUri;

    private static final List<String> unprotectedRoutes = List.of(
            "/driver",
            "/student",
            "/login"
    );

    public Predicate<ServerHttpRequest> isRouteSecured = request -> unprotectedRoutes.stream().noneMatch(uri -> {
        if (request.getURI().getPath().equals(uri)) {
            return request.getMethod().equals(HttpMethod.POST);
        }
        return false;
    });

    public String getCarpoolUri() {
        return carpoolUri;
    }

    public String getUnivanUri() {
        return univanUri;
    }

    public String getRoutesUri() {
        return routesUri;
    }

    public String getHistoryUri() {
        return historyUri;
    }
}
