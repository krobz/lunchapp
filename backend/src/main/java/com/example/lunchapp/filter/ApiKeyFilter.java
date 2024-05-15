package com.example.lunchapp.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * This class implements the Filter interface to provide a filter for validating API keys.
 * The filter checks the "X-API-KEY" header in the incoming request against a valid API key.
 * If the API key is invalid or missing, it sends an HTTP UNAUTHORIZED response.
 */
@Component
public class ApiKeyFilter implements Filter {

    @Value("${api.key}")
    private String validApiKey;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String apiKey = req.getHeader("X-API-KEY");
        if (req.getMethod().equals("OPTIONS") || (apiKey != null && apiKey.equals(validApiKey))) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            HttpServletResponse res = (HttpServletResponse) servletResponse;
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("Invalid API Key");
        }
    }
}
