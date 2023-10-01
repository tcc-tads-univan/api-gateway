package br.tads.ufpr.univangateway.service;

public enum PathMapping {
    UNIVAN("/univan", "/api"),
    CARPOOL("/carpool", "/api"),
    LOGIN("/login", "/api/Authentication/login"),
    DRIVER("/driver", "/api/Driver"),
    STUDENT("/student", "/api/Student");

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
