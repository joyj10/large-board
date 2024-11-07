package com.large.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class BoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoardApplication.class, args);
    }

}
