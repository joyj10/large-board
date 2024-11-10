package com.large.board.controller;

import com.large.board.dto.response.CommonResponse;
import com.large.board.service.AwsSnsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/aws-sns")
public class AwsSnsController {

    private final AwsSnsService awsSnsService;

    @PostMapping("/create-topic")
    public CommonResponse<String> createTopic(@RequestParam(name = "topicName") final String topicName) {
        String topic = awsSnsService.createTopic(topicName);
        return CommonResponse.ok(topic);
    }

    @PostMapping("/subscribe")
    public CommonResponse<String> subscribe(@RequestParam(name = "endpoint") final String endpoint,
                                            @RequestParam(name = "topicArn") final String topicArn) {
        String subscribe = awsSnsService.subscribe(endpoint, topicArn);
        return CommonResponse.ok(subscribe);
    }

    @PostMapping("/publish")
    public CommonResponse<String> publish(@RequestParam String topicArn,
                          @RequestBody Map<String, Object> message) {
        String publishedMessage = awsSnsService.publish(topicArn, message);
        return CommonResponse.ok(publishedMessage);
    }
}
