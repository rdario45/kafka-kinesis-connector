package com.ticketmaster.sponsorship.upsell.stream;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.streams.processor.StateRestoreListener;

@Slf4j
public class StateLogRestoreListener implements StateRestoreListener {

    @Override
    public void onRestoreStart(final TopicPartition topicPartition,
                               final String storeName,
                               final long startingOffset,
                               final long endingOffset) {

        log.info("Started restoration of " + storeName + " partition " + topicPartition.partition());
        log.info(" total records to be restored " + (endingOffset - startingOffset));
    }

    @Override
    public void onBatchRestored(final TopicPartition topicPartition,
                                final String storeName,
                                final long batchEndOffset,
                                final long numRestored) {

        log.debug("Restored batch " + numRestored + " for " + storeName + " partition " + topicPartition.partition());

    }

    @Override
    public void onRestoreEnd(final TopicPartition topicPartition,
                             final String storeName,
                             final long totalRestored) {

        log.info("Restoration complete for " + storeName + " partition " + topicPartition.partition());
    }
}

