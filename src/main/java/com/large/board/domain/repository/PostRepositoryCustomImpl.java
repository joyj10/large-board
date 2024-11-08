package com.large.board.domain.repository;

import com.large.board.common.code.SortStatus;
import com.large.board.domain.entity.PostEntity;
import com.large.board.dto.request.PostSearchRequest;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.large.board.domain.entity.QPostEntity.postEntity;

@Repository
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostEntity> searchPosts(PostSearchRequest searchRequest) {
        return queryFactory
                .selectFrom(postEntity)
                .where(
                        containsByTitle(searchRequest.getTitleKeyword()),
                        containsByContents(searchRequest.getContentKeyword()),
                        eqCategoryId(searchRequest.getCategoryId()),
                        eqUserId(searchRequest.getUserId())
                )
                .orderBy(getOrderSpecifiers(searchRequest.getSortStatus()))
                .fetch();
    }

    private BooleanExpression containsByTitle(String keyword) {
        return keyword != null ? postEntity.title.contains(keyword) : null;
    }

    private BooleanExpression containsByContents(String keyword) {
        return keyword != null ? postEntity.contents.contains(keyword) : null;
    }

    private BooleanExpression eqCategoryId(Long id) {
        return (id != null && id != 0L)? postEntity.categoryEntity.id.eq(id) : null;
    }

    private BooleanExpression eqUserId(Long id) {
        return (id != null && id != 0L) ? postEntity.userEntity.id.eq(id) : null;
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(SortStatus sortStatus) {
        switch (sortStatus) {
            case CATEGORIES:
                return new OrderSpecifier<?>[]{postEntity.categoryEntity.name.asc()};
            case NEWEST:
                return new OrderSpecifier<?>[]{postEntity.createdDate.desc()};
            case OLDEST:
                return new OrderSpecifier<?>[]{postEntity.createdDate.asc()};
            default:
                return new OrderSpecifier<?>[]{postEntity.createdDate.desc()}; // 기본: 최신순
        }
    }
}
