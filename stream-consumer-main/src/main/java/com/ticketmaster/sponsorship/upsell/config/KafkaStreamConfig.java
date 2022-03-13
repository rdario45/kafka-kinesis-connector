package com.ticketmaster.sponsorship.upsell.config;

import com.ticketmaster.sponsorship.upsell.service.DateProvider;
import com.ticketmaster.sponsorship.upsell.stream.StateLogRestoreListener;
import com.ticketmaster.sponsorship.upsell.stream.TopologyFactory;
import com.ticketmaster.sponsorship.upsell.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@Slf4j
public class KafkaStreamConfig {

    @Bean
    KafkaStreams kafkaStreams(@Autowired KafkaProperties kafkaProperties,
                              TopologyFactory topologyFactory) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, kafkaProperties.getConsumerName());
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, LogAndContinueExceptionHandler.class);
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 16);
        KafkaStreams streams = new KafkaStreams(topologyFactory.buildTopology(), props);
        streams.setGlobalStateRestoreListener(new StateLogRestoreListener());

        streams.setUncaughtExceptionHandler((thread, throwable) -> {
            log.error("Kafka streams received an uncaught exception", throwable);
            new Thread(() ->
                    System.exit(1)
            ).start();
        });
        return streams;
    }

    @Bean
    DateProvider dateProvider() {
        return Utils::currentDateTs;
    }
}
