package com.ag.kafka.producer.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class MessageResponse {

    String status;
    final LocalDateTime timestamp = LocalDateTime.now();

}
