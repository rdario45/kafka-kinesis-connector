package com.ticketmaster.sponsorship.upsell.stream;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
@Slf4j
public class StreamRunner {

    private KafkaStreams streams;

    public StreamRunner(KafkaStreams streams) {
        log.info("Streams started");
        this.streams = streams;
    }

    @PostConstruct
    public void run() {
        streams.start();
    }

    @PreDestroy
    public void closeStream() {
        log.info("Streams closed");
        streams.close();
    }
}
