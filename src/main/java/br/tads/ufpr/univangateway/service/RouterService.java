package br.tads.ufpr.univangateway.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RouterService {
    @Value("${microservices.carpool.url}")
    private String carpoolUri;
    @Value("${microservices.univan.url}")
    private String univanUri;

    public String getCarpoolUri() {
        return carpoolUri;
    }

    public String getUnivanUri() {
        return univanUri;
    }
}
