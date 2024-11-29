package com.ag.kafka.consumer.service;

import com.ag.kafka.consumer.common.RateLimiter;
import com.ag.kafka.consumer.exception.RateLimiterException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConsumerService {

    final RateLimiter rateLimiter = new RateLimiter();

    public void consume(ConsumerRecord<String, String> record) {
        if (rateLimiter.tryConsume()) {
            if (record.key().isEmpty()) {
                throw new NullPointerException("Key cannot be empty.");
            }
            log.info(">>> Processed record key={}, value={}.", record.key(), record.value());
        } else {
            throw new RateLimiterException("Rate limit exceeded.");
        }
    }

}
