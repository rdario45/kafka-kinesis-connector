package com.ticketmaster.sponsorship.upsell.stream;

import com.amazonaws.services.kinesis.producer.KinesisProducer;
import com.amazonaws.services.kinesis.producer.KinesisProducerConfiguration;
import com.ticketmaster.sponsorship.upsell.config.KinesisProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KinesisProducerClient {

    private static KinesisProducer INSTANCE;
    private static KinesisProperties properties;

    public KinesisProducerClient(@Autowired KinesisProperties kinesisProperties) {
        this.properties = kinesisProperties;
    }

    static KinesisProducer getInstance() {
        if (INSTANCE == null) {
            KinesisProducerConfiguration config = new KinesisProducerConfiguration()
                    .setRecordMaxBufferedTime(Long.parseLong(properties.getRecordMaxBufferedTime())) // TODO remove parser.
                    .setMaxConnections(Long.parseLong(properties.getMaxConnections()))
                    .setRequestTimeout(Long.parseLong(properties.getRequestTimeout()))
                    .setRegion(properties.getRegion());
            INSTANCE = new KinesisProducer(config);
        }
        return INSTANCE;
    }
}
