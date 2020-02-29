package com.clsaa.dop.server.test.enums;

public enum HttpMethod {

    POST,
    GET,
    PUT,
    PATCH,
    DELETE,
    HEAD,
    OPTIONS;

    private HttpMethod() {
    }

    public static HttpMethod from(String method) {
        for (HttpMethod value : HttpMethod.values()) {
            if (value.toString().equalsIgnoreCase(method)) {
                return value;
            }
        }
        return null;
    }
}
