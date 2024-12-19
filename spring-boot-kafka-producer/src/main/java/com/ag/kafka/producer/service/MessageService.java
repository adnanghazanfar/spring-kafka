package com.ag.kafka.producer.service;

import com.ag.kafka.producer.controller.dto.MessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageService {

    @Value("${kafka.topic}")
    String topicName;

    final KafkaTemplate<String, String> kafkaTemplate;

    public String sendMessage(MessageRequest messageRequest) {
        return kafkaTemplate.send(topicName, messageRequest.getKey(), messageRequest.getValue()).thenApply((SendResult<String, String> result) -> {
            if (result != null && result.getProducerRecord() != null && result.getProducerRecord().value() != null) {
                log.info("Sent topic=[{}], message=[{}]", topicName, result.getProducerRecord().value());
            }
            return "SUCCESS";
        }).exceptionally((Throwable ex) -> {
            log.error("Unable to send topic=[{}], message=[{}] due to : {}", topicName, messageRequest, ex.getMessage());
            return "FAILURE";
        }).join();
    }

}
