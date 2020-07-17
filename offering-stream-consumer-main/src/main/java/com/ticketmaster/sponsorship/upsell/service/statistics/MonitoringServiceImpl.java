package com.ticketmaster.sponsorship.upsell.service.statistics;

import io.prometheus.client.Counter;
import org.springframework.stereotype.Service;

@Service
public class MonitoringServiceImpl implements MonitoringService {

    // TODO move to config file.
    public static final String DISCOVERY_REPLICA_MESSAGE_COUNT = "offering_replica_message_count";
    public static final String COUNT_EVENTS = "Count events";

    private static final Counter messageCounter = Counter.build()
            .name(DISCOVERY_REPLICA_MESSAGE_COUNT)
            .help(COUNT_EVENTS)
            .register();

    @Override
    public void messageSaved(int count) {
        messageCounter.inc(count);
    }
}
