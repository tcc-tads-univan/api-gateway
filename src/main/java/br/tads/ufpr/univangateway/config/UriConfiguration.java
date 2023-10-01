package br.tads.ufpr.univangateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class UriConfiguration {
    @Value("${microservices.carpool.url}")
    private String carpoolService;
    @Value("${microservices.univan.url}")
    private String univanService;

    public String getCarpoolService() {
        return carpoolService;
    }
    public String getUnivanService() {
        return univanService;
    }
}
