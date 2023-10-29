package br.tads.ufpr.univangateway.service;

public enum PathMapping {
    ROUTES("/routes", "/api/routes"),
    CARPOOL("/carpool", "/api"),
    LOGIN("/login", "/api/Authentication/login"),
    DRIVER("/driver", "/api/Driver"),
    STUDENT("/student", "/api/Student"),
    SUBSCRIPTION("/subscription", "/api/Subscription"),
    HISTORY("/history", "/api/History");

    private final String frontendPath;
    private final String backendReplacement;

    PathMapping(String frontendPath, String backendReplacement) {
        this.frontendPath = frontendPath;
        this.backendReplacement = backendReplacement;
    }

    public String getFrontendPath() {
        return frontendPath;
    }

    public String getBackendReplacement() {
        return backendReplacement;
    }
}
