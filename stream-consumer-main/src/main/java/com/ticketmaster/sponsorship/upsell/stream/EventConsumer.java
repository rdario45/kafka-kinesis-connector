package com.ticketmaster.sponsorship.upsell.stream;

import com.ticketmaster.sponsorship.upsell.config.KafkaProperties;
import com.ticketmaster.sponsorship.upsell.service.DateProvider;
import com.ticketmaster.sponsorship.upsell.util.JsonUtils;
import com.ticketmaster.sponsorship.upsell.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.Cancellable;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;

import java.time.Duration;

@Slf4j
public class EventConsumer implements Transformer<String, String, KeyValue<String, Long>> {

    private static final String START_DATE_JSON_PATH = "$.dates.start.localDate";

    private ProcessorContext processorContext;
    private KeyValueStore<String, Long> eventDatesStore;
    private Cancellable cancellable;
    private final KafkaProperties properties;
    private final DateProvider dateProvider;

    public EventConsumer(KafkaProperties properties, DateProvider dateProvider) {
        this.properties = properties;
        this.dateProvider = dateProvider;
    }

    @Override
    public void init(ProcessorContext processorContext) {
        this.eventDatesStore =  (KeyValueStore<String, Long>) processorContext.getStateStore(properties.getEventDateStore());
        this.processorContext = processorContext;
        log.info("Initialize scheduler for batch deleting");
        cancellable = processorContext.schedule(Duration.ofMinutes(properties.getDeleteIntervalMinutes()),
                PunctuationType.WALL_CLOCK_TIME,
                timestamp -> deleteOldEvents());
    }

    private void deleteOldEvents() {
        log.info("Start deleting events. Keystore size: {}", eventDatesStore.approximateNumEntries());
        KeyValueIterator<String, Long> keyValueIterator = eventDatesStore.all();

        while (keyValueIterator.hasNext()) {
            KeyValue<String, Long> keyValue = keyValueIterator.next();
            if (isOld(keyValue.value)) {
                this.processorContext.forward(keyValue.key, keyValue.value);
                this.eventDatesStore.delete(keyValue.key);
            }
        }
        log.info("Ending deleting events. Keystore size: {}", eventDatesStore.approximateNumEntries());
    }

    private boolean isOld(Long ts) {
        return ts != null && (ts < dateProvider.getCurrentTs() - properties.getRetentionDays() * 24 * 60 * 60);
    }

    @Override
    public KeyValue<String, Long> transform(String key, String value) {
        Long ts = Utils.dateToEpoch(JsonUtils.readSilently(value, START_DATE_JSON_PATH));
        if (isOld(ts)) {
            log.info("Event {} with startDate {} is old. Skipped.", key, ts);
        } else if (notValid(key, value)) {
            log.error("Couldn't process message. Key: {}, Value: {}", key, value);
        } else {
            this.eventDatesStore.put(key, ts);
        }
        return null;
    }

    private boolean notValid(String key, String value) {
        return StringUtils.isBlank(key) || StringUtils.isBlank(value) || !Utils.isValidS3Key(key);
    }

    @Override
    public void close() {
        log.info("Cancel scheduler for for batch deleting");
        cancellable.cancel();
    }
}
