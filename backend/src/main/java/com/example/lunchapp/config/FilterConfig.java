package com.example.lunchapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.lunchapp.filter.ApiKeyFilter;


/**
 * This class provides configuration for the filter used to validate API keys for the given URL patterns.
 * The API key filter checks the X-API-KEY header in the incoming request against a valid API key.
 * If the API key is invalid or missing, it sends an HTTP UNAUTHORIZED response.
 */
@Configuration
public class FilterConfig {

    private final ApiKeyFilter apiKeyFilter;

    @Autowired
    public FilterConfig(ApiKeyFilter apiKeyFilter) {
        this.apiKeyFilter = apiKeyFilter;
    }

    @Bean
    public FilterRegistrationBean<ApiKeyFilter> apiKeyFilterOne(){
        FilterRegistrationBean<ApiKeyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(apiKeyFilter);
        registrationBean.addUrlPatterns("/sessions/*");
        registrationBean.addUrlPatterns("/users/*");
        return registrationBean;
    }
}
