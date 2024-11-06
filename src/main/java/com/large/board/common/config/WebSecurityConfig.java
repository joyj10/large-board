package com.large.board.common.config;

import com.large.board.common.code.Role;
import com.large.board.common.filter.AuthorizationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.large.board.common.config.GlobalValue.WHITE_LIST;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 활성화
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) //csrf 공격 보안 disable 처리 (csrf Cross Site Forgery 사이트 간 요청 위조)
            .httpBasic(AbstractHttpConfigurer::disable) // basic authentication filter 비활성화
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))  // 세션이 필요할 때만 생성
            .addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                    authorizationManagerRequestMatcherRegistry
                            // static resource 의 경우 모든 요청 허용 처리
                            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()

                            // swagger & 로그인 & 회원 가입 관련 경로 인증 없이 통과 처리
                            .requestMatchers(WHITE_LIST).permitAll()

                            // open-api 하위 모든 주소는 인증 없이 통과
                            .requestMatchers("/open-api/**").permitAll()

                            // 로그인, 로그아웃 API 시큐리티 인증 없이 통과
                            .requestMatchers("/users/sign-up", "/auth/login").permitAll()

                            // 어드민 권한 필요 API
                            .requestMatchers("/admin/**").hasRole(Role.ADMIN.name())

                            // 그 외 모든 요청 인증 사용
                            .anyRequest().authenticated()
            )
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(logout -> logout
                    .logoutUrl("/auth/logout")
                    .logoutSuccessHandler((request, response, authentication) -> {
                        request.getSession().invalidate(); // 로그아웃 시 세션 무효화
                        response.setStatus(HttpServletResponse.SC_OK);
                    })
            );

        return http.build();
    }
}
