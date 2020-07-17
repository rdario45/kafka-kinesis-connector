package com.ticketmaster.sponsorship.upsell.stream;

import com.ticketmaster.sponsorship.upsell.config.KafkaProperties;
import com.ticketmaster.sponsorship.upsell.config.KinesisProperties;
import com.ticketmaster.sponsorship.upsell.service.DateProvider;
import com.ticketmaster.sponsorship.upsell.service.EventOfferingLoggerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

@Service
@Slf4j
public class TopologyFactory {

    private KafkaProperties properties;
    private KinesisProperties kinesisProperties;
    private EventOfferingLoggerService eventOfferingLoggerService;
    private DateProvider dateProvider;

    public TopologyFactory(KafkaProperties properties,
                           KinesisProperties kinesisProperties,
                           EventOfferingLoggerService eventOfferingLoggerService,
                           DateProvider dateProvider) {
        this.properties = properties;
        this.kinesisProperties = kinesisProperties;
        this.eventOfferingLoggerService = eventOfferingLoggerService;
        this.dateProvider = dateProvider;
    }

    public Topology buildTopology() {
        StreamsBuilder builder = new StreamsBuilder();
        builder.addStateStore(getEventStoreBuilder())
                .stream(properties.getUpsellHostAssignmentsTopic(), Consumed.with(Serdes.String(), Serdes.String()))
                .peek((key, value) -> {
                    try {
                        System.out.println("addUserRecord(\n" +
                                        "stream: " + kinesisProperties.getStream() + " \n" +
                                        "key: " + key  + " \n" +
                                        "value: " + ByteBuffer.wrap(value.getBytes("UTF-8")));
                        KinesisProducerClient.getInstance().addUserRecord(
                                kinesisProperties.getStream(),
                                "myPartitionKey",
                                ByteBuffer.wrap(value.getBytes("UTF-8"))
                        );
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    eventOfferingLoggerService.logEventIfOffering(value);
                })
                .transform(() ->
                        new EventConsumer(properties, dateProvider), properties.getEventDateStore()
                ).to( (key, value, recordContext) -> {
                    System.out.println("key "+key);
                    return null;
                });
        return builder.build();
    }

    private StoreBuilder<KeyValueStore<String, Long>> getEventStoreBuilder() {
        if (properties.isPersistentKeystore()) {
            return Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(properties.getEventDateStore()), Serdes.String(), Serdes.Long());
        } else {
            return Stores.keyValueStoreBuilder(
                    Stores.inMemoryKeyValueStore(properties.getEventDateStore()),
                    Serdes.String(),
                    Serdes.Long()).withLoggingDisabled();
        }
    }

}
