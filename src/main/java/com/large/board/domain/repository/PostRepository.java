package com.large.board.domain.repository;

import com.large.board.domain.entity.PostEntity;
import com.large.board.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long>, PostRepositoryCustom {
    List<PostEntity> findAllByUserEntity(UserEntity userEntity);
    Optional<PostEntity> findByIdAndUserEntity(Long id, UserEntity userEntity);

    long countByIdAndUserEntity(Long id, UserEntity userEntity);
}
