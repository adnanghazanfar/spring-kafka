package com.ag.kafka.consumer.listener;

import com.ag.kafka.consumer.service.ConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaConsumer {

    final ConsumerService consumerService;

    @KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = "first_topic")
    public void consume(
            ConsumerRecord<String, String> record,
            @Header(KafkaHeaders.DELIVERY_ATTEMPT) int attempt) {

        log.info("Received message: record={}, attempt={}.", record, attempt);
        consumerService.consume(record);

    }

}
