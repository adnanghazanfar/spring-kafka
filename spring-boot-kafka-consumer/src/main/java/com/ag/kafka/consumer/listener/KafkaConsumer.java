package com.ag.kafka.consumer.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumer {

    @KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = "first_topic")
    public void consume(ConsumerRecord<String, String> record) {
        if (record.key().contains("k10")) {
            throw new RuntimeException("Retryable exception.");
        } else if (record.key().contains("k11")) {
            throw new NullPointerException("Not retryable expcetion.");
        } else {
            log.info("Consumed message: record={}.", record);
            log.info("\n\n\n\n\n");
        }
    }

}
