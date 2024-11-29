package com.ag.kafka.producer.controller;

import com.ag.kafka.producer.controller.dto.MessageRequest;
import com.ag.kafka.producer.controller.dto.MessageResponse;
import com.ag.kafka.producer.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/producer/messages")
public class MessageController {

    final MessageService messageService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> sendMessage(@Valid @RequestBody MessageRequest messageRequest) {
        var status = messageService.sendMessage(messageRequest);
        return ResponseEntity.ok(MessageResponse.builder().status(status).build());
    }

}
