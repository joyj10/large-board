package com.large.board.domain.repository;

import com.large.board.domain.entity.CommentEntity;
import com.large.board.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    Optional<CommentEntity> findByIdAndUserEntity(Long id, UserEntity userEntity);
}
