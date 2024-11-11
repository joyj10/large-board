package com.large.board.controller.notice;

import com.large.board.service.SlackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/slack")
public class SlackController {

    private final SlackService slackService;

    @GetMapping("/slack/error")
    public void error() {
        slackService.sendSlackMessage("게시판 슬랙 에러 테스트", "error");
    }
}
