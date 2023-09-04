package br.tads.ufpr.univangateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class UriConfiguration {
    private String httpBin = "http://httpbin.org:80";
    private String carpoolService = "http://localhost:7179";

    public String getHttpBin() {
        return httpBin;
    }

    public void setHttpBin(String httpBin) {
        this.httpBin = httpBin;
    }

    public String getCarpoolService() {
        return carpoolService;
    }

    public void setCarpoolService(String carpoolService) {
        this.carpoolService = carpoolService;
    }
}
