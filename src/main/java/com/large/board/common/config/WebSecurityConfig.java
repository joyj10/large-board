package com.large.board.common.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String[] SWAGGER = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable) //csrf 공격 보안 disable 처리 (csrf Cross Site Forgery 사이트 간 요청 위조)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 생성 정책 : RESTful API 적합한 설정
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                // static resou미rce 의 경우 모든 요청 허용 처리
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()

                                // swagger 인증 없이 통과 처리
                                .requestMatchers(SWAGGER).permitAll()

                                // open-api 하위 모든 주소는 인증 없이 통과
                                .requestMatchers("/open-api/**").permitAll()

                                // API 시큐리티 인증 없이 통과 (임시 허용)
                                .requestMatchers("/**").permitAll()

                                // 그 외 모든 요청 인증 사용
                                .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }
}
