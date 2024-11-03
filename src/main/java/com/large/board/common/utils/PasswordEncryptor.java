package com.large.board.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * PasswordEncryptor : 비밀번호 암호화 및 검증을 위한 유틸리티 클래스
 * BCryptPasswordEncoder 사용하여 비밀번호 안전하게 암호화
 * 암호화된 비밀번호와 평문 비밀번호를 비교하는 기능을 제공
 *
 * <p>
 * BCryptPasswordEncoder : BCrypt 해시 알고리즘 사용하여 비밀번호 해시
 * 이 알고리즘은 Salt 자동으로 생성하여 사용, 보안성이 높은 암호화 방식
 * 해시 생성 시 강력한 복잡성 제공, brute force 공격에 대한 저항력 높임
 * </p>
 */
public class PasswordEncryptor {

    private PasswordEncryptor() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    /**
     * 주어진 평문 비밀번호를 암호화합니다.
     *
     * @param rawPassword 암호화할 평문 비밀번호
     * @return 암호화된 비밀번호
     */
    public static String encrypt(String rawPassword) {
        return PASSWORD_ENCODER.encode(rawPassword);
    }

    /**
     * 주어진 평문 비밀번호와 암호화된 비밀번호가 일치하는지 확인합니다.
     *
     * @param rawPassword   비교할 평문 비밀번호
     * @param encodedPassword 데이터베이스에 저장된 암호화된 비밀번호
     * @return 비밀번호가 일치하면 true, 그렇지 않으면 false
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return PASSWORD_ENCODER.matches(rawPassword, encodedPassword);
    }
}
