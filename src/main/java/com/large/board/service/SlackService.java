package com.large.board.service;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Log4j2
@Service
public class SlackService {
    @Value("${slack.token}")
    private String slackToken;

    public void sendSlackMessage(String message, String channel) {
        String channelAddress = "";

        if ("error".equals(channel)) {
            channelAddress = "#모니터링";
        }

        try {
            MethodsClient methodsClient = Slack.getInstance().methods(slackToken);

            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(channelAddress)
                    .text(message)
                    .build();
            methodsClient.chatPostMessage(request);

            log.info("channel : {}", channel);
        } catch (SlackApiException | IOException e) {
            log.error(e.getMessage());
        }
    }
}
