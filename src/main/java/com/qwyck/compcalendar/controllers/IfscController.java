package com.qwyck.compcalendar.controllers;

import java.nio.charset.Charset;
import com.qwyck.compcalendar.templates.Competitions;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class IfscController {

    private static final String IFSC_RESOURCE_URL =
            "http://egw.ifsc-climbing.org/egw/ranking/json.php";

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        builder.additionalMessageConverters(
                new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return builder.build();
    }

    @RequestMapping(value = "/ifsc/json", method = RequestMethod.GET, produces = "application/json")
    public Competitions json(RestTemplate restTemplate) {
        Competitions competitions =
                restTemplate.getForObject(IFSC_RESOURCE_URL, Competitions.class);

        return competitions;
    }

}
