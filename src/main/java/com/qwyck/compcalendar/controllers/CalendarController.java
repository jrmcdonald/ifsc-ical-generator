package com.qwyck.compcalendar.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class CalendarController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

}