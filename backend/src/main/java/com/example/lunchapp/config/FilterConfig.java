package com.example.lunchapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.lunchapp.filter.ApiKeyFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Configuration
public class FilterConfig {

    private final ApiKeyFilter apiKeyFilter;
    private static final Logger log = LogManager.getLogger(FilterConfig.class);

    @Autowired
    public FilterConfig(ApiKeyFilter apiKeyFilter) {
        this.apiKeyFilter = apiKeyFilter;
    }

    @Bean
    public FilterRegistrationBean<ApiKeyFilter> apiKeyFilterRegistration() {
        FilterRegistrationBean<ApiKeyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(apiKeyFilter);
        registrationBean.addUrlPatterns("/sessions/*", "/users/*");
        log.debug("Registering API Key Filter for /sessions/* and /users/*");
        return registrationBean;
    }
}
