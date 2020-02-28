package com.jrmcdonald.ifsc.config;

import com.jrmcdonald.ifsc.handler.CalendarHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> route(CalendarHandler calendarHandler) {
        return RouterFunctions
                .route(GET("/calendar").and(accept(MediaType.APPLICATION_JSON)), calendarHandler::getCalendar)
                .andRoute(POST("/calendar").and(accept(MediaType.APPLICATION_JSON)), calendarHandler::getCalendarByCategory);
    }
}
