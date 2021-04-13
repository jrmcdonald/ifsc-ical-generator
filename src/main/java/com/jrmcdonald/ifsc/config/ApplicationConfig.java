package com.jrmcdonald.ifsc.config;

import com.jrmcdonald.common.ext.spring.datetime.config.DateTimeConfiguration;
import com.jrmcdonald.common.ext.spring.reactive.context.config.ReactiveContextLifterConfiguration;
import com.jrmcdonald.common.ext.spring.reactive.filter.config.ReactiveFilterConfiguration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        DateTimeConfiguration.class,
        ReactiveContextLifterConfiguration.class,
        ReactiveFilterConfiguration.class,
})
public class ApplicationConfig {

}
