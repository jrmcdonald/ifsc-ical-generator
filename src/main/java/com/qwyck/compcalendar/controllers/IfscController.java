package com.qwyck.compcalendar.controllers;

import java.util.List;
import com.qwyck.compcalendar.services.IfscService;
import com.qwyck.compcalendar.templates.Competition;
import com.qwyck.compcalendar.templates.Competitions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;

@RestController
public class IfscController {

    private IfscService service;

    @Autowired
    public IfscController(IfscService service) {
        this.service = service;
    }

    @RequestMapping(value = "/ifsc/json", method = RequestMethod.GET, produces = "application/json")
    public Competitions getJson(@RequestParam(value = "cats", required = false) List<String> cats) {
        Competitions competitions = null;

        if (!StringUtils.isEmpty(cats)) {
            competitions = service.findByCategory(cats);
        } else {
            competitions = service.findAll();
        }

        return competitions;
    }

    @RequestMapping(value = "/ifsc/ical", method = RequestMethod.GET, produces = "text/calendar")
    public String getCal(@RequestParam(value = "cats", required = false) List<String> cats) {
        Competitions competitions = null;

        if (!StringUtils.isEmpty(cats)) {
            competitions = service.findByCategory(cats);
        } else {
            competitions = service.findAll();
        }

        ICalendar cal = new ICalendar();

        competitions.getCompetitions().forEach(comp -> addEvent(comp, cal));
        
        return Biweekly.write(cal).go();
    }

    private void addEvent(Competition competition, ICalendar cal) {
        VEvent event = new VEvent();

        event.setDateStart(competition.getStartDate(), false);
        event.setDateEnd(competition.getEndDate(), false);
        event.setSummary(competition.getName());
        event.setDescription(competition.getHomepage());

        cal.addEvent(event);
    }
}
