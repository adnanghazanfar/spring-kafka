package com.ag.kafka.consumer.common;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bandwidth;

import java.time.Duration;

public class RateLimiter {
    private final Bucket bucket;

    public RateLimiter() {
        Bandwidth limit = Bandwidth.simple(1, Duration.ofSeconds(5));
        this.bucket = Bucket.builder().addLimit(limit).build();
    }

    public boolean tryConsume() {
        return bucket.tryConsume(1);
    }
}
