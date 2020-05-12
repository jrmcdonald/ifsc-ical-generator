package com.jrmcdonald.ifsc.config;

import com.jrmcdonald.ifsc.filter.WebFluxFilter;
import com.jrmcdonald.ifsc.logging.WebFluxLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    WebFluxFilter webFluxFilter(@Value("${spring.application.name}") String applicationName, WebFluxLogger webFluxLogger)  {
        return new WebFluxFilter(applicationName, webFluxLogger);
    }

    @Bean
    WebFluxLogger webFluxLogger() {
        return new WebFluxLogger();
    }
}
