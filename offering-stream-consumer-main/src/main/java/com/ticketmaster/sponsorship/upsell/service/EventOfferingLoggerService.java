package com.ticketmaster.sponsorship.upsell.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketmaster.sponsorship.upsell.model.offering.Event;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
public class EventOfferingLoggerService {

    // TODO move to secret store
    private static final String OFFERING_ID = "KZAyXgnZfZ7v7nJ";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void logEventIfOffering(String event) {
        try {
            Optional.of(event)
                    .map(this::convertMessagePartly)
                    .filter(this::isUpsellOfferingEvent)
                    .ifPresent(this::logEvent);
        } catch (Exception ex) {
            log.warn("Error logging offering information:" + ex.getMessage(), ex);
        }
    }

    boolean isUpsellOfferingEvent(Event event) {
        return true;
//        return Optional.ofNullable(event.getClassifications())
//                .orElse(Collections.emptyList())
//                .stream()
//                .map(Classification::getType)
//                .anyMatch(this::isOfferingType);
//    }
//
//    private boolean isOfferingType(ClassificationValue type) {
//        return type != null && OFFERING_ID.equals(type.getId());
    }

    private void logEvent(Event event) {
        log.info("Upsell offering event found: {}", event);
    }

    @SneakyThrows(IOException.class)
    private Event convertMessagePartly(String payload) {
        Event event = objectMapper.readValue(payload, Event.class);
        log.debug("Payload is successfully parsed to {}", event);
        return event;
    }
}
