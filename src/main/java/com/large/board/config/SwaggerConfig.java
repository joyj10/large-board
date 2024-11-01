package com.large.board.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

	@Value("${server.servlet.context-path}")
	private String conTextPath;

	@Bean
	public OpenAPI customOpenAPI() {
		Server server = new Server().url(conTextPath);

		return new OpenAPI()
				.servers(List.of(server))
				.info(new io.swagger.v3.oas.models.info.Info()
						.title("Board Project")
						.version("v1")
						.description("대용량 트래픽 및 데이터 게시판 인터페이스 명세서"));
	}
}
