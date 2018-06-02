package com.qwyck.compcalendar.controllers;

import com.qwyck.compcalendar.services.IfscService;
import com.qwyck.compcalendar.templates.Competitions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IfscController {

    private IfscService service;

    @Autowired
    public IfscController(IfscService service) {
        this.service = service;
    }

    @RequestMapping(value = "/ifsc/json", method = RequestMethod.GET, produces = "application/json")
    public Competitions getJson() {
        return service.findAll();
    }

}
