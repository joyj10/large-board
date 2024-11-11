package com.large.board;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class RedisTest {

    private static GenericContainer redisContainer;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @BeforeAll
    public static void startRedisContainer() {
        // Redis 컨테이너 설정
        redisContainer = new GenericContainer("redis:latest")
                .withExposedPorts(6379)
                .waitingFor(Wait.forListeningPort());
        redisContainer.start();
    }

    @AfterAll
    public static void stopRedisContainer() {
        if (redisContainer != null) {
            redisContainer.stop();
        }
    }

    @Test
    public void testRedis() {
        // Redis에 값 저장
        redisTemplate.opsForValue().set("key", "value");

        // Redis에서 값 가져오기
        String value = redisTemplate.opsForValue().get("key");

        // 값 검증
        assertEquals("value", value);
    }
}
