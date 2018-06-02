package com.qwyck.compcalendar.services;

import java.nio.charset.Charset;
import com.qwyck.compcalendar.templates.Competitions;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * IfscRepository
 */
@Service
public class IfscService {
    
    private static final String IFSC_RESOURCE_URL =
            "http://egw.ifsc-climbing.org/egw/ranking/json.php";

    private final RestTemplate restTemplate;

    public IfscService(RestTemplateBuilder builder) {
        builder.additionalMessageConverters(
                new StringHttpMessageConverter(Charset.forName("UTF-8")));

        restTemplate = builder.build();
    }

    public Competitions findAll() {
        return restTemplate.getForObject(IFSC_RESOURCE_URL, Competitions.class);
    }

}
