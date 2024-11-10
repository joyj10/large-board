package com.large.board.service;

import com.large.board.common.config.AwsConfig;
import com.large.board.common.exception.NoticeException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class AwsSnsService {
    private final AwsConfig awsConfig;

    // SNS 클라이언트 생성 메서드
    private SnsClient createSnsClient() {
        AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(awsConfig.getAwsAccessKey(), awsConfig.getAwsSecretKey())
        );
        return SnsClient.builder()
                .credentialsProvider(credentialsProvider)
                .region(Region.of(awsConfig.getAwsRegion()))
                .build();
    }

    public String createTopic(@NotNull String topicName) {
        CreateTopicRequest request = CreateTopicRequest.builder()
                .name(topicName)
                .build();

        SnsClient snsClient = createSnsClient();

        CreateTopicResponse topicResponse;
        try {
            topicResponse = snsClient.createTopic(request);
        } catch (SnsException e) {
            throw new NoticeException("AWS SNS > Failed to create topic: " + e.awsErrorDetails().errorMessage());
        } finally {
            snsClient.close();
        }

        // 응답 처리: 실패 시 예외 처리
        if (!topicResponse.sdkHttpResponse().isSuccessful()) {
            throw new NoticeException("AWS SNS > Failed to create topic(response) : " + topicResponse);
        }

        log.info("Successfully created Topic. Create Topic ARN: {} = " + topicResponse.topicArn());
        return topicResponse.topicArn();
    }

    public String subscribe(@NotNull String endpoint, @NotNull String topicArn) {
        SubscribeRequest subscribeRequest = SubscribeRequest.builder()
                .protocol("https")
                .topicArn(topicArn)
                .endpoint(endpoint)
                .build();

        SnsClient snsClient = createSnsClient();

        SubscribeResponse subscribeResponse;
        try {
            subscribeResponse = snsClient.subscribe(subscribeRequest);
        } catch (SnsException e) {
            throw new NoticeException("AWS SNS > Failed to subscribe: " + e.awsErrorDetails().errorMessage());
        } finally {
            snsClient.close();
        }

        // 응답 처리: 실패 시 예외 처리
        if (!subscribeResponse.sdkHttpResponse().isSuccessful()) {
            throw new NoticeException("AWS SNS > Failed to subscribe(response): " + subscribeResponse.sdkHttpResponse().statusText());
        }

        log.info("Successfully subscribed. Topic ARN: {}, Subscription ARN: {}", topicArn, subscribeResponse.subscriptionArn());
        return subscribeResponse.subscriptionArn();
    }

    public String publish(@NotNull String topicArn, Map<String, Object> message) {
        PublishRequest publishRequest = PublishRequest.builder()
                .topicArn(topicArn)
                .subject("HTTP ENDPOINT TEST MESSAGE")
                .message(message.toString())
                .build();

        SnsClient snsClient = createSnsClient();
        PublishResponse publishResponse = snsClient.publish(publishRequest);
        log.info("message status:" + publishResponse.sdkHttpResponse().statusCode());
        snsClient.close();

        return "sent message ID = " + publishResponse.messageId();
    }
}
