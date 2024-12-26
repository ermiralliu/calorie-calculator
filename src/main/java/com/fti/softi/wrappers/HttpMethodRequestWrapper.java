package com.fti.softi.wrappers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;

public class HttpMethodRequestWrapper extends HttpServletRequestWrapper {
    private final String method;

    public HttpMethodRequestWrapper(HttpServletRequest request, String method) {
        super(request);
        this.method = method;
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public String getQueryString() {
        String queryString = super.getQueryString();
        if (queryString != null && queryString.contains("_method=")) {
            queryString = queryString.replaceAll("_method=[^&]*&?", "");
            // Remove trailing & if exists
            if (queryString.endsWith("&")) {
                queryString = queryString.substring(0, queryString.length() - 1);
            }
            // Handle the case where the _method parameter is the only parameter
            if (queryString.isEmpty()) {
                return null;
            }
        }
        return queryString;
    }

    @Override
    public String getParameter(String name) {
        if ("_method".equals(name)) {
            return null;
        }
        return super.getParameter(name);
    }

    @Override
    public String[] getParameterValues(String name) {
        if ("_method".equals(name)) {
            return null;
        }
        return super.getParameterValues(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameterMap = new HashMap<>(super.getParameterMap());
        parameterMap.remove("_method");
        return parameterMap;
    }
}

