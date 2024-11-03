package com.large.board.domain.repository;

import com.large.board.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    long countByUserId(String userId);

    @Query("select u from UserEntity u where u.userId = :userId and u.status != 'DELETED'")
    Optional<UserEntity> findActiveUserByUserId(String userId);

    @Query("select u from UserEntity u where u.id = :id and u.status != 'DELETED'")
    Optional<UserEntity> findActiveUserById(Long id);
}
