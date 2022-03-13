package com.ticketmaster.sponsorship.upsell.config;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties("kafka")
@Validated
@Data
public class KafkaProperties {
    @NotEmpty
    private String bootstrapServers;
    @NotEmpty
    private String upsellHostAssignmentsTopic;
    @NotEmpty
    private String consumerName;
    @NotEmpty
    private String eventDateStore;
    @NotNull
    private Long retentionDays;

    private boolean persistentKeystore = true;
    @NotNull
    private Long deleteIntervalMinutes;
}

