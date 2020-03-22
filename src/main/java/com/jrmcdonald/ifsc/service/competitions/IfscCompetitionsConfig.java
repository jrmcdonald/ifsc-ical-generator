package com.jrmcdonald.ifsc.service.competitions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "external.ifsc")
public class IfscCompetitionsConfig {

    @NotEmpty
    private String host;
}
