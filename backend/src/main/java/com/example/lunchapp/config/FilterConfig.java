package com.example.lunchapp.config;

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

    @Bean
    public FilterRegistrationBean<ApiKeyFilter> apiKeyFilterOne(){
        FilterRegistrationBean<ApiKeyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ApiKeyFilter());
        // add api used here
        registrationBean.addUrlPatterns("/sessions/*");
        registrationBean.addUrlPatterns("/users/*");
        return registrationBean;
    }
}
