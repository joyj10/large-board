package com.large.board.common.filter;

import com.large.board.common.exception.UnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.large.board.common.config.GlobalValue.WHITE_LIST;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        // 화이트리스트에 포함된 경로는 인증 검사를 하지 않음
        for (String whiteListPath : WHITE_LIST) {
            whiteListPath = "^/api" + whiteListPath.replace("**", ".*$"); // 정규식 와일드카드(*)로 변경
            if (requestURI.matches(whiteListPath)) {
                filterChain.doFilter(request, response);
                return; // 화이트리스트 경로일 경우, 필터링을 통과하고 이후 로직은 실행되지 않음
            }
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new UnauthorizedException("인증이 되어 있지 않습니다.");
        }
        filterChain.doFilter(request, response);
    }
}
