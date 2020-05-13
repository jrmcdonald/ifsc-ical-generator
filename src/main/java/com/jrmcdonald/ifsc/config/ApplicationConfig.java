package com.jrmcdonald.ifsc.config;

import com.jrmcdonald.ifsc.filter.WebFluxContextFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public WebFluxContextFilter webFluxFilter(@Value("${spring.application.name}") String applicationName)  {
        return new WebFluxContextFilter(applicationName);
    }
}
