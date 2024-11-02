package com.large.board.domain.repository;

import com.large.board.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    long countByUserId(String userId);
}
