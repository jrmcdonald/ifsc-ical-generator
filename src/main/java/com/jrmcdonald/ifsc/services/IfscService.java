package com.jrmcdonald.ifsc.services;

import java.util.List;
import java.nio.charset.Charset;
import com.jrmcdonald.ifsc.templates.Competitions;
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

    public Competitions findByCategory(List<String> cats) {
        Competitions competitions = restTemplate.getForObject(IFSC_RESOURCE_URL, Competitions.class);

        competitions.getCompetitions().removeIf(comp -> !cats.contains(comp.getCat()));

        return competitions;
    }

    public Competitions findByCategoryTwo(List<String> cats) {
        Competitions competitions = restTemplate.getForObject(IFSC_RESOURCE_URL, Competitions.class);

        competitions.getCompetitions().removeIf(comp -> !cats.contains(comp.getCat()));

        return competitions;
    }

}
