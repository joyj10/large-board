package com.large.board.domain.repository;

import com.large.board.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByAccountId(String accountId);

    long countByAccountId(String accountId);

    @Query("select u from UserEntity u where u.accountId = :accountId and u.status <> 'DELETED'")
    Optional<UserEntity> findActiveUserByAccountId(String accountId);

    @Query("select u from UserEntity u where u.id = :id and u.status <> 'DELETED'")
    Optional<UserEntity> findActiveUserById(Long id);
}
