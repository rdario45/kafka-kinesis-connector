package com.ticketmaster.sponsorship.upsell.config;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties("kinesis")
@Validated
@Data
public class KinesisProperties {
    @NotEmpty
    private String recordMaxBufferedTime;
    @NotEmpty
    private String maxConnections;
    @NotEmpty
    private String requestTimeout;
    @NotEmpty
    private String region;
    @NotEmpty
    private String stream;
}

