package com.ag.kafka.consumer.config;

import com.ag.kafka.consumer.exception.RateLimiterException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.ContainerCustomizer;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ConsumerAwareRecordRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@EnableKafka
@Configuration
public class KafkaConfiguration {

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:19092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "ag");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        config.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, CooperativeStickyAssignor.class.getName());

        return new DefaultKafkaConsumerFactory<>(config);
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        // default error handler
        var defaultErrorHandler = new DefaultErrorHandler(recoverer(), new FixedBackOff(500L, 5L));
        defaultErrorHandler.addRetryableExceptions(RateLimiterException.class);
        defaultErrorHandler.addNotRetryableExceptions(NullPointerException.class);
        // listener container factory.
        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerFactory());
        factory.setCommonErrorHandler(defaultErrorHandler);
        return factory;
    }

    // called in the end to log or send it to dlq etc.
    @Bean
    public ConsumerAwareRecordRecoverer recoverer() {
        return (record, consumer, exception)
                -> log.info("Recovered from error: record={}, exception={}.", record, exception.getMessage());
    }

    // required for sending delivery attempts.
    @Bean
    public ContainerCustomizer<String, String, ConcurrentMessageListenerContainer<String, String>> containerCustomizer(
            ConcurrentKafkaListenerContainerFactory<String, String> factory) {
        ContainerCustomizer<String, String, ConcurrentMessageListenerContainer<String, String>> containerCustomizer =
                container -> container.getContainerProperties().setDeliveryAttemptHeader(true);
        factory.setContainerCustomizer(containerCustomizer);
        return containerCustomizer;
    }


}