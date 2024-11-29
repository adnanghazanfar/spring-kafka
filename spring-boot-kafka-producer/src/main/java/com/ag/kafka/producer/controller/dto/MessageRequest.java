package com.ag.kafka.producer.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest {

    @NotEmpty(message = "key can not be a null or empty")
    String key;

    @NotEmpty(message = "value can not be a null or empty")
    String value;

}
